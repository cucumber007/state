package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.gtd2.element.Task


sealed interface Gtd2Action : AppAction {
    sealed class Action : Gtd2Action {
        override fun toString(): String = javaClass.simpleName
    }

    data class DynalistStateUpdated(val dynalistState: DynalistState) : Action()

    // not in View action because used in TaskView on multiple screens
    data class OnTaskClick(val task: Task) : Action()

    // not in View action because used in TaskView on multiple screens
    data class OnTaskLongClick(val task: Task) : Action()
    data class ToggleTask(val task: Task) : Action()

}
