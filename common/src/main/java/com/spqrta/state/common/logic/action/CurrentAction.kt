package com.spqrta.state.common.logic.action


sealed interface CurrentAction : AppAction {
    sealed class Action : CurrentAction {
        override fun toString(): String = javaClass.simpleName
    }

}
