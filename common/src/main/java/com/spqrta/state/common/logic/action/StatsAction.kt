package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.Gtd2State


sealed interface StatsAction : AppAction {
    sealed class Action : StatsAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class UpdateStatsAction(
        val newState: Gtd2State,
        val oldState: Gtd2State
    ) : Action()
}