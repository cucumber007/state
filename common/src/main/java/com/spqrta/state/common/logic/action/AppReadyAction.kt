package com.spqrta.state.common.logic.action

sealed interface AppReadyAction : AppAction {
    sealed class Action : AppReadyAction {
        override fun toString(): String = javaClass.simpleName
    }

    object FlipResetStateEnabledAction : Action()
    object ResetDayAction : Action(), Gtd2Action
}
