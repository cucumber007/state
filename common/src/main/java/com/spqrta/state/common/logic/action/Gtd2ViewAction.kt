package com.spqrta.state.common.logic.action

sealed interface Gtd2ViewAction : AppAction, TinderAction {
    sealed class Action : Gtd2ViewAction {
        override fun toString(): String = javaClass.simpleName
    }
}
