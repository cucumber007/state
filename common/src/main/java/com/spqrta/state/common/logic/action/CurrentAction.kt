package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.gtd2.element.Queue


sealed interface CurrentAction : AppAction {
    sealed class Action : CurrentAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnElementClicked(val element: Queue) : Action()
}