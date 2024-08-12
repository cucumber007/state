package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.CurrentAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.effect.ActionEffect
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.illegalAction
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
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

    private fun reduce(
        action: CurrentAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (val activeElement = state.currentState.activeElement) {
            is ActiveElement.ActiveQueue -> {
                when (action) {
                    is CurrentAction.OnElementClick -> {
                        illegalAction(action, state)
                    }

                    is CurrentAction.OnSubElementClick -> {
                        if (action.element.status == TaskStatus.Active) {
                            optActiveTask.set(
                                state,
                                TimeredTask(
                                    action.element, TimeredState.Paused()
                                )
                            ).withEffects()
                        } else {
                            state.withEffects()
                        }
                    }

                    is CurrentAction.OnTimerPause -> {
                        optTimeredState.get(state)?.let { oldTimerState ->
                            optTimeredState.set(
                                state,
                                TimeredState.Paused(oldTimerState.timePassed)
                            ).withEffects()
                        } ?: illegalAction(action, state)
                    }

                    is CurrentAction.OnTimerStart -> {
                        optTimeredState.get(state)?.let { oldTimerState ->
                            optTimeredState.set(
                                state,
                                TimeredState.Running(
                                    oldTimerState.timePassed,
                                    LocalTime.now()
                                )
                            ).withEffects()
                        } ?: illegalAction(action, state)
                    }

                    is CurrentAction.OnTimerReset -> {
                        reduce(
                            CurrentAction.OnTimerStart,
                            state
                        )
                    }

                    is ClockAction.TickAction -> {
                        when (val timerState = optTimeredState.get(state)) {
                            is TimeredState.Running -> {
                                val newState = timerState.copy(
                                    passed = (
                                            timerState.passed.totalSeconds + (
                                                    action.time.toLocalTime().toSecondOfDay()
                                                            - timerState.updatedAt.toSecondOfDay()
                                                    )
                                            ).toSeconds(),
                                    updatedAt = action.time.toLocalTime()
                                )
                                optTimeredState.set(state, newState).withEffects()
                            }

                            is TimeredState.Paused, null -> {
                                state.withEffects()
                            }
                        }
                    }

                    is CurrentAction.OnSubElementLongClick -> {
                        state.withEffects(ActionEffect(Gtd2Action.ToggleTask(action.element)))
                    }

                    is Gtd2Action.ToggleTask -> {
                        state.taskTree.getElement(activeElement.queue.name)!!
                            .let { it as Queue }
                            .let { newActiveQueue ->
                                optActiveElement.set(
                                    state,
                                    ActiveElement.ActiveQueue(
                                        queue = newActiveQueue,
                                        activeTask = newActiveQueue.tasks()
                                            // check if active task is still not done/deactivated
                                            .firstOrNull { it.name == activeElement.activeTask?.task?.name && it.status == TaskStatus.Active }
                                            ?.let {
                                                activeElement.activeTask
                                            }
                                    )
                                ).withEffects()
                            }
                    }
                }
            }

            // no ActiveElement
            null -> {
                when (action) {
                    is CurrentAction.OnElementClick -> {
                        state.copy(
                            currentState = state.currentState.copy(
                                activeElement = ActiveElement.ActiveQueue(
                                    action.element,
                                    activeTask = null
                                )
                            )
                        ).withEffects()
                    }

                    is ClockAction.TickAction,
                    is Gtd2Action.ToggleTask -> {
                        state.withEffects()
                    }

                    is CurrentAction.OnSubElementClick,
                    is CurrentAction.OnSubElementLongClick,
                    CurrentAction.OnTimerPause,
                    CurrentAction.OnTimerReset,
                    CurrentAction.OnTimerStart -> {
                        illegalAction(action, state)
                    }
                }
            }
        }
    }
}