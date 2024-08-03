package com.spqrta.state.common.logic.features.daily.personas

import com.spqrta.state.common.logic.PromptsEnabled
import com.spqrta.state.common.logic.action.ProductiveActivityAction
import com.spqrta.state.common.logic.action.TimerAction
import com.spqrta.state.common.logic.features.daily.personas.productive.Flipper
import com.spqrta.state.common.logic.features.daily.personas.productive.Navigation
import com.spqrta.state.common.logic.features.daily.personas.productive.ToDoList
import com.spqrta.state.common.logic.features.daily.personas.productive.ToDoListScreen
import com.spqrta.state.common.logic.features.daily.timers.WorkTimer
import com.spqrta.state.common.logic.features.global.ActionEffect
import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.util.IllegalActionException
import com.spqrta.state.common.util.optics.asOpticOptional
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Productive(
    val promptsEnabled: PromptsEnabled = PromptsEnabled,
    val navigation: Navigation = ToDoListScreen,
    val flipper: Flipper = Flipper(),
    val toDoList: ToDoList = ToDoList(),
    val activity: ActivityState = None,
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

        val optToDoList = ({ state: Productive ->
            state.toDoList
        } to { state: Productive, subState: ToDoList ->
            state.copy(toDoList = subState)
        }).asOpticOptional()

        val optNavigation = ({ state: Productive ->
            state.navigation
        } to { state: Productive, subState: Navigation ->
            state.copy(navigation = subState)
        }).asOpticOptional()

        fun reduce(
            action: ProductiveActivityAction,
            state: ActivityState
        ): Reduced<out ActivityState, out AppEffect> {
            return when (action) {
                is ProductiveActivityAction.ActivityDone -> {
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

                is ProductiveActivityAction.NeedMoreTime -> {
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
                                reduce(ProductiveActivityAction.ActivityDone(state), state)
                            } else state.withEffects()
                        }

                        Fiz, None -> state.withEffects()
                    }
                }
            }
        }
    }
}
