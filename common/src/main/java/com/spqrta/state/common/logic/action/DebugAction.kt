package com.spqrta.state.common.logic.action


sealed interface DebugAction : AppAction {
    sealed class Action : DebugAction {
        override fun toString(): String = javaClass.simpleName
    }

    object FlipClockMode : Action(), ClockAction
    object FlipResetStateEnabled : AppReadyAction.Action()
    object ResetDay : AppReadyAction.Action(), Gtd2Action
}
