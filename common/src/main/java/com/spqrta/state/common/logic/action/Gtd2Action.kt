package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.Task

sealed interface Gtd2Action : AppAction {
    sealed class Action : Gtd2Action {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnTaskClickAction(val task: Task) : Action()
}
