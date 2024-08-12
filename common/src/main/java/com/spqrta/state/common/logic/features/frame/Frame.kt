package com.spqrta.state.common.logic.features.frame

import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.FrameTabsAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.alarms.Alarms
import com.spqrta.state.common.logic.features.dynalist.Dynalist
import com.spqrta.state.common.logic.features.gtd2.Gtd2
import com.spqrta.state.common.logic.features.gtd2.current.Current
import com.spqrta.state.common.logic.features.gtd2.tinder.Tinder
import com.spqrta.state.common.logic.features.stats.Stats
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.plus
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects

object Frame {
    val reducer = widen(
        typeGet<AppAction, FrameTabsAction>(),
        AppStateOptics.optReady + AppReadyOptics.optFrameState,
        ::reduce,
    ) + ::reduceAppAction

    private fun reduceAppAction(
        action: AppAction,
        state: AppState
    ): Reduced<out AppState, out AppEffect> {
        return when (state) {
            AppNotInitialized -> state.withEffects()
            is AppReady -> when (state.frameState) {
                FrameState.TabAlarms -> Alarms.reducer(action, state)
                FrameState.TabCurrent -> Current.reducer(action, state)
                FrameState.TabDynalist -> Dynalist.viewReducer(action, state)
                FrameState.TabGtd2 -> Gtd2.reducer(action, state)
                FrameState.TabStats -> Stats.reducer(action, state)
                FrameState.TabTinder -> Tinder.reducer(action, state)
            }
        }
    }

    private fun reduce(
        action: FrameTabsAction,
        state: FrameState
    ): Reduced<out FrameState, out AppEffect> {
        return when (action) {
            is FrameTabsAction.OnTabClicked -> {
                action.tab.withEffects()
            }
        }
    }
}