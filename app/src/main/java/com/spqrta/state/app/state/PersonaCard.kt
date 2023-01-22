package com.spqrta.state.app.state

import com.spqrta.state.app.AppEffect
import com.spqrta.state.util.ReducerResult
import com.spqrta.state.util.withEffects
import com.spqrta.state.util.wrap

sealed class PersonaCard {
    override fun toString(): String = javaClass.simpleName

    sealed interface PersonaCardAction
    sealed class Action: AppAction, PersonaCardAction
    object GetBackAction: Action()

    companion object {
        fun reduce(action: PersonaCardAction, state: AppState): ReducerResult<out AppState, out AppEffect> {
            return wrap(action, state, AppState.opticPersona) { _, oldPersona ->
                UndefinedPersona.withEffects()
            }
        }

        const val I_AM_BACK = "I am back!"
    }
}
object UndefinedPersona: PersonaCard() {
    fun reduce(
        action: UndefinedPersonaAction,
        state: AppState
    ): ReducerResult<out AppState, out AppEffect> {
        return when(action) {
            is DefinePersonaAction -> {
                wrap(action, state, AppState.opticPersona) { _, oldPersona ->
                    action.persona.withEffects()
                }
            }
        }
    }

    sealed interface UndefinedPersonaAction
    sealed class Action: AppAction, UndefinedPersonaAction
    data class DefinePersonaAction(val persona: PersonaCard): Action()
}

object Productive: PersonaCard()
object Depressive: PersonaCard()
object Unstable: PersonaCard()
object Explosive: PersonaCard()
