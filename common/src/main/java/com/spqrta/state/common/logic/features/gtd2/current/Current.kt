package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.CurrentAction
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.effect.ActionEffect
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.SendNotificationEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.illegalAction
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.testLog
import com.spqrta.state.common.util.time.toSeconds
import java.time.LocalTime

object Current {
    private val optActiveElement = Gtd2State.optCurrent + CurrentState.optActiveElement
    private val optActiveTask = optActiveElement + ActiveElement.optActiveTask
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
        ::reduceView,
    )

    private fun reduceView(
        action: CurrentViewAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (val activeElement = state.currentState.activeElement) {
            is ActiveElement.ActiveQueue -> {
                when (action) {
                    is CurrentViewAction.OnElementClick -> {
                        illegalAction(action, state)
                    }

                    is CurrentViewAction.OnTaskComplete -> {
                        val activeTask = activeElement.activeTask
                        if (activeTask != null) {
                            state.withEffects(ActionEffect(Gtd2Action.ToggleTask(activeTask.task)))
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
                        optActiveTask.get(state)?.let { activeTask ->
                            val oldTimeredState = activeTask.timeredState
                            when (oldTimeredState) {
                                is TimeredState.Paused -> {
                                    val effects = mutableSetOf<AppEffect>()
                                    var newTimeredState = TimeredState.Running(
                                        passed = oldTimeredState.timePassed,
                                        updatedAt = LocalTime.now(),
                                        notificationSent = oldTimeredState.notificationSent
                                    )

                                    // send notification
                                    if (activeTask.isTimerOverdue && !oldTimeredState.notificationSent) {
                                        newTimeredState = newTimeredState.copy(
                                            notificationSent = true
                                        )
                                        effects.add(
                                            SendNotificationEffect(
                                                "Timer is up for ${optActiveTask.get(state)!!.task.name}",
                                            )
                                        )
                                    }

                                    optTimeredState.set(state, newTimeredState).withEffects(effects)
                                        .also {
                                            testLog(newTimeredState)
                                        }
                                }

                                is TimeredState.Running -> {
                                    illegalAction(action, state)
                                }
                            }

                        } ?: illegalAction(action, state)
                    }

                    is CurrentViewAction.OnTimerReset -> {
                        optActiveTask.get(state)?.let { activeTask ->
                            val oldTimeredState = activeTask.timeredState
                            val newTimeredState = when (oldTimeredState) {
                                is TimeredState.Paused -> TimeredState.Paused.INITIAL
                                is TimeredState.Running -> TimeredState.Running(
                                    passed = 0.toSeconds(),
                                    updatedAt = LocalTime.now(),
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
                                )
                            ).withEffects()
                        } else {
                            state.withEffects()
                        }
                    }

                    is CurrentViewAction.OnSubElementLongClick -> {
                        state.withEffects(ActionEffect(Gtd2Action.ToggleTask(action.element)))
                    }

                    is CurrentViewAction.OnSkipTask -> {
                        val activeTask = activeElement.activeTask
                        if (activeTask != null) {
                            val tasks = activeElement.activeTasks
                            if (tasks.isEmpty()) {
                                // do nothing, it's the only task left
                                state.withEffects()
                            } else {
                                val currentIndex =
                                    tasks.indexOfFirst { it.name == activeTask.task.name }
                                val newIndex = if (currentIndex < tasks.size - 1) {
                                    currentIndex + 1
                                } else {
                                    0
                                }
                                val nextTask = tasks[newIndex]
                                optActiveElement.set(
                                    state,
                                    activeElement.copy(
                                        activeTask = TimeredTask(
                                            nextTask,
                                            TimeredState.Paused.INITIAL
                                        )
                                    )
                                ).withEffects()
                            }
                        } else {
                            illegalAction(action, state)
                        }
                    }
                }
            }

            // no ActiveElement
            null -> {
                when (action) {
                    is CurrentViewAction.OnElementClick -> {
                        state.copy(
                            currentState = state.currentState.copy(
                                activeElement = ActiveElement.ActiveQueue(
                                    action.element,
                                    activeTask = null
                                )
                            )
                        ).withEffects()
                    }

                    is CurrentViewAction.OnSkipTask,
                    is CurrentViewAction.OnTaskComplete,
                    is CurrentViewAction.OnSubElementClick,
                    is CurrentViewAction.OnSubElementLongClick,
                    is CurrentViewAction.OnTimerPause,
                    is CurrentViewAction.OnTimerReset,
                    is CurrentViewAction.OnTimerStart -> {
                        illegalAction(action, state)
                    }
                }
            }
        }
    }

    private fun reduce(
        action: CurrentAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is ClockAction.TickAction -> {
                optActiveTask.get(state)?.let { activeTask ->
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
                                    )
                                )
                            }
                            optTimeredState.set(state, newTimeredState).withEffects(effects)
                        }

                        is TimeredState.Paused, null -> {
                            state.withEffects()
                        }
                    }
                } ?: state.withEffects()
            }

            is Gtd2Action.ToggleTask -> {
                onNewState(state)
            }

            is DebugAction.ResetDay -> {
                onNewState(state)
            }

            is StateLoadedAction -> {
                onNewState(state)
            }
        }
    }

    private fun onNewState(state: Gtd2State): Reduced<out Gtd2State, out AppEffect> {
        return when (val activeElement = state.currentState.activeElement) {
            is ActiveElement.ActiveQueue -> {
                val optActiveElement = Gtd2State.optCurrent + CurrentState.optActiveElement
                val activeTask = activeElement.activeTask

                val newActiveQueue = state.taskTree.getElement(activeElement.queue.name) as Queue
                var newActiveElement = activeElement.copy(
                    queue = newActiveQueue,
                    activeTask = null
                )
                val newActiveTask = activeTask?.let {
                    val newStateOfActiveTask =
                        state.taskTree.getElement(activeTask.task.name) as Task
                    when (newStateOfActiveTask.status) {
                        is TaskStatus.Active -> {
                            activeTask
                        }

                        is TaskStatus.Done, TaskStatus.Inactive -> {
                            null
                        }
                    }
                } ?: run {
                    // the previous active task was completed or there wasn't any
                    if (newActiveElement.activeTasks.isNotEmpty()) {
                        TimeredTask(
                            newActiveElement.activeTasks.first(),
                            TimeredState.Paused.INITIAL
                        )
                    } else {
                        null
                    }
                }
                newActiveElement = newActiveElement.copy(activeTask = newActiveTask)

                val newState = optActiveElement.set(
                    state,
                    newActiveElement
                )
                newState.withEffects()
            }

            null -> {
                val queuesToChoose = state.taskTree.queues().filter { it.isLeafGroup() }
                val newActiveElement =
                    queuesToChoose.let { queues ->
                        if (queues.size == 1) {
                            queues.first().let { queue ->
                                ActiveElement.ActiveQueue(
                                    queue, queue.tasks().firstOrNull()?.let {
                                        TimeredTask(
                                            it,
                                            TimeredState.Paused.INITIAL
                                        )
                                    }
                                )
                            }
                        } else {
                            null
                        }
                    }
                if (newActiveElement != null) {
                    optActiveElement.set(state, newActiveElement).withEffects()
                } else {
                    Gtd2State.optCurrent.set(
                        state,
                        state.currentState.copy(queuesToChoose = queuesToChoose)
                    ).withEffects()
                }
            }
        }
    }
}
