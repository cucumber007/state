package com.spqrta.state.app.action

sealed interface ToDoListAction : AppAction {
    sealed class Action : ToDoListAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnPress(val title: String) : Action()
}
