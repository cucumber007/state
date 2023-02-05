package com.spqrta.state.app.action

import com.spqrta.state.app.features.daily.personas.ActivityState
import com.spqrta.state.app.features.daily.personas.Persona
import com.spqrta.state.app.features.daily.personas.Work

sealed interface PersonaAction : AppAction {
    sealed class Action : PersonaAction
    object GetBackAction : Action()
}

sealed interface UndefinedPersonaAction : AppAction {
    sealed class Action : UndefinedPersonaAction
    data class DefinePersonaAction(val persona: Persona) : Action() 
}

sealed interface ProductiveAction : AppAction {
    sealed class Action : ProductiveAction
    data class ActivityDone(val activity: ActivityState) : Action()
    data class NeedMoreTime(val activity: Work) : Action()
}

