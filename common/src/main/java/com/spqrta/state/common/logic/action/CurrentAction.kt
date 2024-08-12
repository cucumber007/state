package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task


sealed interface CurrentAction : AppAction {
    sealed class Action : CurrentAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnElementClick(val element: Queue) : Action()
    data class OnSubElementClick(val element: Task) : Action()
    data class OnSubElementLongClick(val element: Task) : Action()
    object OnTimerStart : Action()
    object OnTimerPause : Action()
    object OnTimerReset : Action()
}