package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.ToBeDone


sealed interface CurrentViewAction : AppAction {
    sealed class Action : CurrentViewAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnElementClick(val element: Queue) : Action()
    data class OnSubElementClick(val element: ToBeDone) : Action()
    data class OnSubElementLongClick(val element: ToBeDone) : Action()
    object OnResetActiveElementClick : Action()
    object OnScrollToActiveClick : Action()
    object OnSkipTask : Action()
    object OnTaskComplete : Action()
    object OnTimerPause : Action()
    object OnTimerReset : Action()
    object OnTimerStart : Action()
    object OnToggleShowDoneClick : Action()
}
