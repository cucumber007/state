package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.frame.FrameState

sealed interface FrameTabsAction : AppAction {
    sealed class Action : FrameTabsAction
    data class OnTabClicked(val tab: FrameState) : Action()
}