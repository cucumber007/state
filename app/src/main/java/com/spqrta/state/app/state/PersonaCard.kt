package com.spqrta.state.app.state

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.util.state_machine.ReducerResult
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.wrap
import kotlinx.serialization.Serializable

@Serializable
sealed class PersonaCard {
    override fun toString(): String = javaClass.simpleName

    sealed interface PersonaCardAction : AppAction
    sealed class Action : PersonaCardAction
    object GetBackAction : Action()

    companion object {
        fun reduce(action: PersonaCardAction, state: AppReady): ReducerResult<out AppReady, out AppEffect> {
            return when(action) {
                GetBackAction -> {
                    wrap(state, AppReadyOptics.optPersona) { oldPersona ->
                        UndefinedPersona.withEffects()
                    }
                }
            }
        }

        const val I_AM_BACK = "I am back!"
    }
}

@Serializable
object UndefinedPersona : PersonaCard() {
    fun reduce(
        action: UndefinedPersonaAction,
        state: AppReady
    ): ReducerResult<out AppReady, out AppEffect> {
        return when (action) {
            is DefinePersonaAction -> {
                wrap(state, AppReadyOptics.optPersona) { oldPersona ->
                    action.persona.withEffects()
                }
            }
        }
    }

    sealed interface UndefinedPersonaAction : AppAction
    sealed class Action : UndefinedPersonaAction
    data class DefinePersonaAction(val persona: PersonaCard) : Action()
}

@Serializable
object Productive : PersonaCard()

@Serializable
object Depressed : PersonaCard()

@Serializable
object Unstable : PersonaCard()

@Serializable
object Irritated : PersonaCard()
