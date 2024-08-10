package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.element.Task

sealed interface Gtd2Action : AppAction, TinderAction {
    sealed class Action : Gtd2Action {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnTaskClickAction(val task: Task) : Action()

    data class OnTaskLongClickAction(val task: Task) : Action()
}
