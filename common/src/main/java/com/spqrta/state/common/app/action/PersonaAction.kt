package com.spqrta.state.common.app.action

import com.spqrta.state.common.app.features.daily.personas.ActivityState
import com.spqrta.state.common.app.features.daily.personas.Persona
import com.spqrta.state.common.app.features.daily.personas.Work

sealed interface PersonaAction : AppAction {
    sealed class Action : PersonaAction {
        override fun toString(): String = javaClass.simpleName
    }

    object GetBackAction : Action()
}

sealed interface UndefinedPersonaAction : AppAction {
    sealed class Action : UndefinedPersonaAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class DefinePersonaAction(val persona: Persona) : Action()
}

sealed interface ProductiveActivityAction : AppAction {
    sealed class Action : ProductiveActivityAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class ActivityDone(val activity: ActivityState) : Action()
    data class NeedMoreTime(val activity: Work) : Action()
}

sealed interface ProductiveNavigationAction : AppAction {
    sealed class Action : ProductiveNavigationAction {
        override fun toString(): String = javaClass.simpleName
    }

    object OpenTodoListClicked : Action()
}
