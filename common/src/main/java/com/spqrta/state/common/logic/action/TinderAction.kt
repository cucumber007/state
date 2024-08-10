package com.spqrta.state.common.logic.action


sealed interface TinderAction : AppAction {
    sealed class Action : TinderAction {
        override fun toString(): String = javaClass.simpleName
    }

}