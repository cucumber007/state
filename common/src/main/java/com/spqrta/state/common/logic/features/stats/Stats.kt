package com.spqrta.state.common.logic.features.stats

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.withEffects

object Stats {
    val reducer = ::reduce

    private fun reduce(action: AppAction, state: AppState): Reduced<out AppState, out AppEffect> {
        return state.withEffects()
    }

    fun calculate(appReady: AppReady): StatsState {
        val gtd2State = appReady.gtd2State
        return StatsState()
    }
}
