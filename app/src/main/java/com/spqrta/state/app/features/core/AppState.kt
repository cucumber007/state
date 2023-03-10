package com.spqrta.state.app.features.core

import com.spqrta.state.app.*
import com.spqrta.state.app.action.OnResumeAction
import com.spqrta.state.app.features.daily.clock_mode.ClockMode
import com.spqrta.state.app.features.daily.clock_mode.Update
import com.spqrta.state.app.features.daily.DailyState
import com.spqrta.state.app.features.daily.personas.*
import com.spqrta.state.app.features.daily.timers.Timers
import com.spqrta.state.app.features.stats.Stats
import com.spqrta.state.app.features.storage.Storage
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.app.state.optics.AppReadyOptics.optDailyState
import com.spqrta.state.app.state.optics.AppReadyOptics.optStats
import com.spqrta.state.util.*
import com.spqrta.state.util.optics.*
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalTime

sealed class AppState {
    override fun toString(): String = javaClass.simpleName
}

object AppNotInitialized : AppState()

@Suppress("RemoveRedundantQualifierName")
@Serializable
data class AppReady(
    val dailyState: DailyState,
    val clockMode: ClockMode = Update,
    @Contextual
    val timers: Timers = Timers(),
    val stats: Stats = Stats(),
    val storage: Storage = Storage(),
) : AppState() {

    companion object {
        val INITIAL = AppReady(DailyState.INITIAL)

        fun reduce(action: OnResumeAction, state: AppReady): Reduced<out AppReady, out AppEffect> {
            return wrap(state, optDailyState, optStats) { oldDailyState, oldStats ->
                if (action.datetime.isAfter(LocalDateTime.of(oldDailyState.date, LocalTime.of(5, 0)))) {
                    (DailyState.INITIAL to updateStats(oldStats, oldDailyState)).withEffects()
                } else {
                    (oldDailyState to oldStats).withEffects()
                }
            }
        }

        private fun updateStats(oldStats: Stats, oldDay: DailyState): Stats {
            return oldStats
        }
    }
}
