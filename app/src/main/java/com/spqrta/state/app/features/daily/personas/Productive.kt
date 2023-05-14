package com.spqrta.state.app.features.daily.personas

import com.spqrta.state.app.ActionEffect
import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.PromptsEnabled
import com.spqrta.state.app.action.ProductiveAction
import com.spqrta.state.app.action.TimerAction
import com.spqrta.state.app.features.daily.personas.productive.Flipper
import com.spqrta.state.app.features.daily.timers.WorkTimer
import com.spqrta.state.util.IllegalActionException
import com.spqrta.state.util.optics.asOpticOptional
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Productive(
    val promptsEnabled: PromptsEnabled = PromptsEnabled,
    val flipper: Flipper = Flipper(),
    val activity: ActivityState = None
) : Persona() {
    companion object {
        val optActivity = ({ state: Productive ->
            state.activity
        } to { state: Productive, subState: ActivityState ->
            state.copy(activity = subState)
        }).asOpticOptional()

        val optFlipper = ({ state: Productive ->
            state.flipper
        } to { state: Productive, subState: Flipper ->
            state.copy(flipper = subState)
        }).asOpticOptional()

        fun reduce(
            action: ProductiveAction,
            state: ActivityState
        ): Reduced<out ActivityState, out AppEffect> {
            return when (action) {
                is ProductiveAction.ActivityDone -> {
                    if (state::class != action.activity::class) {
                        throw IllegalActionException(action, state)
                    } else {
                        when (state) {
                            Fiz -> {
                                Work(WorkTimer).withEffects(
                                    ActionEffect(TimerAction.StartTimer(WorkTimer))
                                )
                            }

                            None -> {
                                Fiz.withEffects()
                            }

                            is Work -> {
                                Fiz.withEffects()
                            }
                        }
                    }
                }

                is ProductiveAction.NeedMoreTime -> {
                    when (state) {
                        is Work -> {
                            state.withEffects(
                                ActionEffect(
                                    TimerAction.ProlongTimerAction(
                                        state.timer,
                                        5.toSeconds()
                                    )
                                )
                            )
                        }

                        Fiz, None -> {
                            throw IllegalActionException(action, state)
                        }
                    }

                }

                is TimerAction.TimerEnded -> {
                    when (state) {
                        is Work -> {
                            if (action.timerId == state.timer) {
                                reduce(ProductiveAction.ActivityDone(state), state)
                            } else state.withEffects()
                        }

                        Fiz, None -> state.withEffects()
                    }
                }
            }
        }
    }
}
