package com.spqrta.state.common.logic.features.stats

import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.withEffects

object Stats {
    val reducer = ::reduce

    private fun reduce(action: AppAction, state: AppState): Reduced<out AppState, out AppEffect> {
        return state.withEffects()
    }
}