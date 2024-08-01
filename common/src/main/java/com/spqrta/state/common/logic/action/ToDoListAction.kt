package com.spqrta.state.common.logic.action

sealed interface ToDoListAction : AppAction {
    sealed class Action : ToDoListAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnPress(val title: String) : Action()
}
