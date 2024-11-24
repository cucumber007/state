package com.spqrta.state.common.logic.features.gtd2.current

import android.annotation.SuppressLint
import com.spqrta.dynalist.utility.pure.Optional
import com.spqrta.dynalist.utility.pure.toOptional
import com.spqrta.state.common.environments.DateTimeEnvironment
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.CurrentAction
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.effect.ActionEffect
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.PlaySoundEffect
import com.spqrta.state.common.logic.effect.SendNotificationEffect
import com.spqrta.state.common.logic.effect.ViewEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.use_case.play_sound.Sound
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.effectIf
import com.spqrta.state.common.util.state_machine.illegalAction
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.state_machine.withOptic
import com.spqrta.state.common.util.log.testLog
import com.spqrta.state.common.util.time.toSeconds

@SuppressLint("NewApi")
object Current {
    private val optActiveElement = Gtd2State.optCurrent + CurrentState.optActiveElement
    private val optActiveTask = optActiveElement + ActiveElement.optActiveTask
    private val optActiveQueue = Gtd2State.optCurrent + CurrentState.optActiveQueue
    private val optTimeredState =
        Gtd2State.optCurrent + CurrentState.optActiveElement + ActiveElement.optActiveTask + TimeredTask.optTimeredState

    val reducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optGtd2State,
        ::reduce,
    )

    val viewReducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optGtd2State,
        ::viewReduce,
    )

    private fun reduce(
        action: CurrentAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is ClockAction.TickAction -> {
                optActiveTask.get(state)?.toNullable()?.let { activeTask ->
                    when (val timeredState = activeTask.timeredState) {
                        is TimeredState.Running -> {
                            val effects = mutableSetOf<AppEffect>()

                            // bump timer
                            var newTimeredState = timeredState.copy(
                                passed = (
                                        timeredState.passed.totalSeconds + (
                                                action.time.toLocalTime().toSecondOfDay()
                                                        - timeredState.updatedAt.toSecondOfDay()
                                                )
                                        ).toSeconds(),
                                updatedAt = action.time.toLocalTime()
                            )

                            // send notification
                            if (activeTask.isTimerOverdue && !timeredState.notificationSent) {
                                newTimeredState = newTimeredState.copy(
                                    notificationSent = true
                                )
                                optTimeredState.set(state, newTimeredState)
                                effects.add(
                                    SendNotificationEffect(
                                        "Timer is up for ${activeTask.task.name}",
                                    ),
                                )
                                effects.add(
                                    PlaySoundEffect(Sound.Flute)
                                )
                            }
                            optTimeredState.set(state, newTimeredState).withEffects(effects)
                        }

                        is TimeredState.Paused -> {
                            state.withEffects()
                        }
                    }
                } ?: state.withEffects()
            }

            is DebugAction.ResetDay -> {
                withOptic(
                    action,
                    state.currentState,
                    CurrentState.optActiveElement,
                ) { activeElement ->
                    when (activeElement) {
                        is ActiveElement.ActiveQueue -> {
                            getNextActiveTask(
                                activeElement,
                                activeElement.activeTask.toNullable(),
                                state
                            ).flatMapState {
                                ActiveElement.optActiveTask.set(
                                    activeElement,
                                    it.newState
                                )
                            }
                        }
                    }
                }.flatMapState {
                    Gtd2State.optCurrent.set(state, it.newState)
                }
            }
        }
    }

    private fun viewReduce(
        action: CurrentViewAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (val activeElement = state.currentState.activeElement) {
            is ActiveElement.ActiveQueue -> viewReduceActiveQueue(action, state, activeElement)
            null -> viewReduceNoActiveElement(action, state)
        }
    }

    private fun viewReduceActiveQueue(
        action: CurrentViewAction,
        state: Gtd2State,
        activeElement: ActiveElement.ActiveQueue
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is CurrentViewAction.OnElementClick -> {
                illegalAction(action, state)
            }

            is CurrentViewAction.OnResetActiveElementClick -> {
                withOptic(
                    action,
                    state,
                    Gtd2State.optCurrent,
                ) {
                    it.copy(
                        activeElement = null
                    ).withEffects()
                }
            }

            is CurrentViewAction.OnTaskComplete -> {
                val activeTask = activeElement.activeTask.toNullable()
                if (activeTask != null) {
                    getNextActiveTask(
                        activeElement,
                        activeTask,
                        state,
                        skip = true
                    ).flatMapState {
                        ActiveElement.optActiveTask.set(activeElement, it.newState)
                    }.flatMapState {
                        CurrentState.optActiveElement.set(state.currentState, it.newState)
                    }.flatMapState {
                        Gtd2State.optCurrent.set(state, it.newState)
                    }.flatMapEffects {
                        it.effects + ActionEffect(Gtd2Action.ToggleTask(activeTask.task))
                    }
                } else illegalAction(action, state)
            }

            is CurrentViewAction.OnTimerPause -> {
                optTimeredState.get(state)?.let { oldTimerState ->
                    when (oldTimerState) {
                        is TimeredState.Paused -> {
                            illegalAction(action, state)
                        }

                        is TimeredState.Running -> {
                            optTimeredState.set(
                                state,
                                TimeredState.Paused(
                                    passed = oldTimerState.timePassed,
                                    notificationSent = oldTimerState.notificationSent
                                )
                            ).withEffects()
                        }
                    }
                } ?: illegalAction(action, state)
            }

            is CurrentViewAction.OnTimerStart -> {
                optActiveTask.get(state)?.toNullable()?.let { activeTask ->
                    when (val oldTimeredState = activeTask.timeredState) {
                        is TimeredState.Paused -> {
                            val effects = mutableSetOf<AppEffect>()
                            var newTimeredState = TimeredState.Running(
                                passed = oldTimeredState.timePassed,
                                updatedAt = DateTimeEnvironment.timeNow,
                                notificationSent = oldTimeredState.notificationSent
                            )

                            // send notification
                            if (activeTask.isTimerOverdue && !oldTimeredState.notificationSent) {
                                newTimeredState = newTimeredState.copy(
                                    notificationSent = true
                                )
                                effects.add(
                                    SendNotificationEffect(
                                        "Timer is up for ${
                                            optActiveTask.get(state)!!.toNullable()!!.task.name
                                        }",
                                    )
                                )
                            }

                            optTimeredState.set(state, newTimeredState).withEffects(effects)
                        }

                        is TimeredState.Running -> {
                            illegalAction(action, state)
                        }
                    }

                } ?: illegalAction(action, state)
            }

            is CurrentViewAction.OnTimerReset -> {
                optActiveTask.get(state)?.toNullable()?.let { activeTask ->
                    val oldTimeredState = activeTask.timeredState
                    val newTimeredState = when (oldTimeredState) {
                        is TimeredState.Paused -> TimeredState.Paused.INITIAL
                        is TimeredState.Running -> TimeredState.Running(
                            passed = 0.toSeconds(),
                            updatedAt = DateTimeEnvironment.timeNow,
                            notificationSent = false
                        )
                    }
                    optTimeredState.set(state, newTimeredState).withEffects()
                } ?: illegalAction(action, state)
            }

            is CurrentViewAction.OnSubElementClick -> {
                if (action.element.status == TaskStatus.Active) {
                    optActiveTask.set(
                        state,
                        TimeredTask(
                            action.element, TimeredState.Paused.INITIAL
                        ).toOptional()
                    ).withEffects()
                } else {
                    state.withEffects()
                }
            }

            is CurrentViewAction.OnSubElementLongClick -> {
                state.withEffects(ActionEffect(Gtd2Action.ToggleTask(action.element)))
            }

            is CurrentViewAction.OnSkipTask -> {
                val activeTask = activeElement.activeTask.toNullable()
                if (activeTask != null) {
                    getNextActiveTask(
                        activeElement,
                        activeTask,
                        state,
                        skip = true
                    ).flatMapState {
                        ActiveElement.optActiveTask.set(activeElement, it.newState)
                    }.flatMapState {
                        CurrentState.optActiveElement.set(state.currentState, it.newState)
                    }.flatMapState {
                        Gtd2State.optCurrent.set(state, it.newState)
                    }
                } else {
                    illegalAction(action, state)
                }
            }

            is CurrentViewAction.OnToggleShowDoneClick -> {
                state.copy(
                    currentState = state.currentState.copy(
                        showDone = !state.currentState.showDone
                    )
                ).withEffects()
            }

            is CurrentViewAction.OnScrollToActiveClick -> {
                optActiveTask.get(state)?.toNullable()?.let { activeTask ->
                    state.currentState.tasksToShowValue(state.tasksState).indexOf(activeTask.task)
                        .let { index ->
                            state.withEffects(
                                ViewEffect.Scroll(
                                    index,
                                )
                            )
                        }
                } ?: illegalAction(action, state)
            }
        }
    }

    private fun viewReduceNoActiveElement(
        action: CurrentViewAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is CurrentViewAction.OnElementClick -> {
                state.copy(
                    currentState = state.currentState.copy(
                        activeElement = ActiveElement.ActiveQueue(
                            action.element.name,
                            activeTask = Optional.nullValue()
                        )
                    )
                ).withEffects()
            }

            is CurrentViewAction.OnResetActiveElementClick,
            is CurrentViewAction.OnScrollToActiveClick,
            is CurrentViewAction.OnSkipTask,
            is CurrentViewAction.OnSubElementClick,
            is CurrentViewAction.OnSubElementLongClick,
            is CurrentViewAction.OnTaskComplete,
            is CurrentViewAction.OnTimerPause,
            is CurrentViewAction.OnTimerReset,
            is CurrentViewAction.OnTimerStart,
            is CurrentViewAction.OnToggleShowDoneClick,
                -> {
                illegalAction(action, state)
            }
        }
    }

    private fun getNextActiveTask(
        activeElement: ActiveElement.ActiveQueue,
        activeTask: TimeredTask?,
        state: Gtd2State,
        skip: Boolean = false
    ): Reduced<Optional<TimeredTask>, out AppEffect> {
        val tasks = activeElement.activeTasksValue(state.tasksState)
        return if (tasks.isEmpty()) {
            // do nothing, it's the only task left
            Optional.nullValue<TimeredTask>().withEffects<Optional<TimeredTask>, PlaySoundEffect>()
        } else {
            val nextTask = if (skip) {
                if (activeTask != null) {
                    val currentIndex = tasks.indexOfFirst { it.name == activeTask.task.name }
                    val newIndex = if (currentIndex < tasks.size - 1) {
                        currentIndex + 1
                    } else {
                        0
                    }
                    tasks[newIndex]
                } else {
                    tasks[0]
                }
            } else {
                tasks[0]
            }
            TimeredTask(
                nextTask,
                TimeredState.Paused.INITIAL
            ).toOptional().withEffects(
                effectIf(skip) {
                    PlaySoundEffect(Sound.Ping)
                }
            )
        }
    }
}
