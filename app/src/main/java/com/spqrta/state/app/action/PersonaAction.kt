package com.spqrta.state.app.action

import com.spqrta.state.app.features.daily.personas.Persona

sealed interface PersonaAction : AppAction {
    sealed class Action : PersonaAction
    object GetBackAction : Action()
}

sealed interface UndefinedPersonaAction : AppAction {
    sealed class Action : UndefinedPersonaAction
    data class DefinePersonaAction(val persona: Persona) : Action() 
}

