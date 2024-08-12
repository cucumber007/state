package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.element.Task


sealed interface Gtd2Action : AppAction {
    sealed class Action : Gtd2Action {
        override fun toString(): String = javaClass.simpleName
    }

    data class CompleteTask(val task: Task) : Action(), CurrentAction
}