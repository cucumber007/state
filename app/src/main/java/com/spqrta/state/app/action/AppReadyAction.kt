package com.spqrta.state.app.action

sealed interface AppReadyAction : AppAction {
    sealed class Action: AppReadyAction
    object ResetDayAction : Action()
}
