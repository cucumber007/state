package com.spqrta.state.common.logic.action


sealed interface DebugAction : AppAction {
    sealed class Action : DebugAction {
        override fun toString(): String = javaClass.simpleName
    }

    // The actions extend AppReadyAction because it's legacy (they were handled in AppReady
    // reducer). Create a separate DebugReducer?
    object FlipClockMode : Action(), ClockAction
    object FlipResetStateEnabled : AppReadyAction.Action()
    object ResetState : AppReadyAction.Action(), Gtd2Action, DynalistAction
    object ResetDay : DebugAction, Gtd2Action, CurrentAction
    object SendTestNotification : AppReadyAction.Action()
    object UpdateDynalist : DynalistAction {
        override fun toString(): String = javaClass.simpleName
    }
}
