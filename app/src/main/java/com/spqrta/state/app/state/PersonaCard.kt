package com.spqrta.state.app.state

import com.spqrta.state.app.AppEffect
import com.spqrta.state.util.ReducerResult
import com.spqrta.state.util.withEffects
import com.spqrta.state.util.wrap
import kotlinx.serialization.Serializable

@Serializable
sealed class PersonaCard {
    override fun toString(): String = javaClass.simpleName

    sealed interface PersonaCardAction
    sealed class Action : AppAction, PersonaCardAction
    object GetBackAction : Action()

    companion object {
        fun reduce(
            action: PersonaCardAction,
            state: AppReady
        ): ReducerResult<out AppReady, out AppEffect> {
            return wrap(action, state, AppReadyOptics.optPersona) { _, oldPersona ->
                UndefinedPersona.withEffects()
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
                wrap(action, state, AppReadyOptics.optPersona) { _, oldPersona ->
                    action.persona.withEffects()
                }
            }
        }
    }

    sealed interface UndefinedPersonaAction
    sealed class Action : AppAction, UndefinedPersonaAction
    data class DefinePersonaAction(val persona: PersonaCard) : Action()
}

@Serializable
object Productive : PersonaCard()

@Serializable
object Depressive : PersonaCard()

@Serializable
object Unstable : PersonaCard()

@Serializable
object Explosive : PersonaCard()
