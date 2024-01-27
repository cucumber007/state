package com.spqrta.state.common.app.action

import com.spqrta.state.common.app.features.daily.DailyState

sealed interface AppReadyAction : AppAction {
    sealed class Action : AppReadyAction {
        override fun toString(): String = javaClass.simpleName
    }

    object FlipResetStateEnabledAction : Action()
    data class ResetDayAction(val defaultState: DailyState) : Action()
}
