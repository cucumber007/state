package com.spqrta.state.app.features.daily.personas

import com.spqrta.state.app.ActionEffect
import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.PromptsEnabled
import com.spqrta.state.app.action.PersonaAction
import com.spqrta.state.app.action.PersonaAction.*
import com.spqrta.state.app.action.ProductiveAction
import com.spqrta.state.app.action.ProductiveAction.*
import com.spqrta.state.app.action.TimerAction
import com.spqrta.state.app.action.UndefinedPersonaAction
import com.spqrta.state.app.action.UndefinedPersonaAction.*
import com.spqrta.state.app.features.daily.timers.WorkTimer
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.util.IllegalActionException
import com.spqrta.state.util.optics.asOpticOptional
import com.spqrta.state.util.optics.plus
import com.spqrta.state.util.optics.typeGet
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.state_machine.plus
import com.spqrta.state.util.state_machine.widen
import com.spqrta.state.util.toSeconds
import kotlinx.serialization.Serializable

@Serializable
sealed class Persona {
    open val name: String = javaClass.simpleName
    override fun toString(): String = name

    companion object {
        val optProductive = ({ state: Persona ->
            if(state is Productive) state else null
        } to { state: Persona, subState: Productive ->
            subState as Persona
        }).asOpticOptional()

        val reducer = widen(
            typeGet(),
            AppReadyOptics.optPersona,
            Persona::reduce
        ) + widen(
            typeGet(),
            AppReadyOptics.optPersona,
            UndefinedPersona::reduce
        ) + widen(
            typeGet(),
            AppReadyOptics.optPersona + optProductive + Productive.optActivity,
            Productive::reduce
        )

        fun reduce(action: PersonaAction, state: Persona): Reduced<out Persona, out AppEffect> {
            return when (action) {
                GetBackAction -> {
                    UndefinedPersona.withEffects()
                }
            }
        }

        const val I_AM_BACK = "I am back!"
        const val I_AM_NOT_GOOD = "I am not good"
    }
}

@Serializable
object UndefinedPersona : Persona() {
    fun reduce(
        action: UndefinedPersonaAction,
        state: Persona
    ): Reduced<out Persona, out AppEffect> {
        return when (action) {
            is DefinePersonaAction -> {
                action.persona.withEffects()
            }
        }
    }
}

@Serializable
data class Productive(
    val promptsEnabled: PromptsEnabled = PromptsEnabled,
    val activity: ActivityState = None
) : Persona() {
    companion object {
        val optActivity = ({ state: Productive ->
            state.activity
        } to { state: Productive, subState: ActivityState ->
            state.copy(activity = subState)
        }).asOpticOptional()

        fun reduce(
            action: ProductiveAction,
            state: ActivityState
        ): Reduced<out ActivityState, out AppEffect> {
            return when (action) {
                is ActivityDone -> {
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
                is NeedMoreTime -> {
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
                    when(state) {
                        is Work -> {
                            if(action.timerId == state.timer) {
                                reduce(ActivityDone(state), state)
                            } else state.withEffects()
                        }
                        Fiz, None -> state.withEffects()
                    }
                }
            }
        }
    }
}

@Serializable
object Depressed : Persona()

@Serializable
object Unstable : Persona()

@Serializable
object Irritated : Persona()
