package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.element.Task

sealed interface Gtd2ViewAction : AppAction, TinderAction {
    sealed class Action : Gtd2ViewAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnTaskClick(val task: Task) : Action()

    data class OnTaskLongClick(val task: Task) : Action()
}
