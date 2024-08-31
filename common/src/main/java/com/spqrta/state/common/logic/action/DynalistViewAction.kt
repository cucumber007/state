package com.spqrta.state.common.logic.action


sealed interface DynalistViewAction : AppAction {
    sealed class Action : DynalistViewAction {
        override fun toString(): String = javaClass.simpleName
    }


}
