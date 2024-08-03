package com.spqrta.state.common.logic.features.frame

import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.features.alarms.Alarms
import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.withEffects

object Frame {
    val reducer = ::reduce

    fun reduce(action: AppAction, state: AppState): Reduced<out AppState, out AppEffect> {
        return when (state) {
            AppNotInitialized -> state.withEffects()
            is AppReady -> when (state.frameState) {
                FrameState.TabAlarms -> Alarms.reducer(action, state)
                FrameState.TabGtd2 -> Gtd2.reducer(action, state)
            }
        }
    }

}