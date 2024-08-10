package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderPrompt
import com.spqrta.state.common.util.time.TimeValue


sealed interface TinderAction : AppAction {
    sealed class Action : TinderAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnEstimated(
        val element: Element,
        val estimate: TimeValue
    ) : Action()

    data class OnSkipped(
        val prompt: TinderPrompt
    ) : Action()
}