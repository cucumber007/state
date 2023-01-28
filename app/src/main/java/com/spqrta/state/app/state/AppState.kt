package com.spqrta.state.app.state

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.util.*
import com.spqrta.state.util.state_machine.ReducerResult
import com.spqrta.state.util.state_machine.withEffects
import kotlinx.serialization.Serializable

sealed class AppState

object AppNotInitialized : AppState()

@Suppress("RemoveRedundantQualifierName")
@Serializable
data class AppReady(
    val dailyState: DailyState,
    val stats: Stats = Stats()
) : AppState() {
    companion object {
        val INITIAL = AppReady(DailyState.INITIAL)

        fun reduce(action: OnResumeAction, state: AppReady): ReducerResult<out AppReady, out AppEffect> {
            return wrap(state, AppReady.optDailyState, AppReady.optStats) { oldDailyState, oldStats ->
                if (action.date != oldDailyState.date) {
                    (DailyState.INITIAL to updateStats(oldStats, oldDailyState)).withEffects()
                } else {
                    (oldDailyState to oldStats).withEffects()
                }
            }
        }

        private fun updateStats(oldStats: Stats, oldDay: DailyState): Stats {
            return oldStats
        }

        val optStats = AppReadyOptics.optStats
        val optPersona = AppReadyOptics.optPersona
        private val optDailyState = AppReadyOptics.optDailyState
    }
}

