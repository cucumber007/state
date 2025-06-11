package com.spqrta.state.common.logic.action


sealed interface StatsAction : AppAction {
    sealed class Action : StatsAction {
        override fun toString(): String = javaClass.simpleName
    }
}
