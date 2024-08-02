package com.spqrta.state.common.logic.action

sealed interface Gtd2Action : AppAction {
    sealed class Action : Gtd2Action {
        override fun toString(): String = javaClass.simpleName
    }

}
