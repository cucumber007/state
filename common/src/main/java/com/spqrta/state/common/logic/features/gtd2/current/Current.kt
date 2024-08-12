package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.CurrentAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
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
                    is CurrentAction.OnElementSelected -> {
                        state.copy(
                            currentState = state.currentState.copy(
                                activeElement = ActiveElement.ActiveQueue(action.element)
                            )
                        ).withEffects()
                    }

                    is CurrentAction.OnSubElementSelected -> {
                        optActiveTask.set(
                            state,
                            TimeredTask(
                                action.element, TimeredState.Paused()
                            )
                        ).withEffects()
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
                }
            }

            null -> {
                when (action) {
                    is CurrentAction.OnElementSelected -> {
                        state.copy(
                            currentState = state.currentState.copy(
                                activeElement = ActiveElement.ActiveQueue(action.element)
                            )
                        ).withEffects()
                    }

                    is ClockAction.TickAction -> {
                        state.withEffects()
                    }

                    is CurrentAction.OnSubElementSelected,
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