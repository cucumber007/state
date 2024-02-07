package com.spqrta.state.common.app.features.core

import com.spqrta.state.common.app.AppEffect
import com.spqrta.state.common.app.action.AppReadyAction
import com.spqrta.state.common.app.action.OnResumeAction
import com.spqrta.state.common.app.features.daily.DailyState
import com.spqrta.state.common.app.features.daily.clock_mode.ClockMode
import com.spqrta.state.common.app.features.daily.clock_mode.Update
import com.spqrta.state.common.app.features.daily.timers.Timers
import com.spqrta.state.common.app.features.stats.Stats
import com.spqrta.state.common.app.features.storage.Storage
import com.spqrta.state.common.app.state.optics.AppReadyOptics.optDailyState
import com.spqrta.state.common.app.state.optics.AppReadyOptics.optStats
import com.spqrta.state.common.util.optics.identityOptional
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.optics.wrap
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

sealed class AppState {
    override fun toString(): String = javaClass.simpleName
}

object AppNotInitialized : AppState()

@Serializable
data class AppReady(
    val dailyState: DailyState,
    val clockMode: ClockMode = Update,
    @Contextual
    val timers: Timers = Timers(),
    val stats: Stats = Stats(),
    val storage: Storage = Storage(),
    val resetStateEnabled: Boolean = false,
) : AppState() {

    companion object {
        val INITIAL = AppReady(DailyState.INITIAL)
        val INITIAL_WATCH = AppReady(
            dailyState = DailyState.INITIAL_WATCH,
            resetStateEnabled = true
        )

        val reducer = widen(
            typeGet(),
            identityOptional(),
            AppReady::reduce,
        )

        fun reduce(action: AppReadyAction, state: AppReady): Reduced<out AppReady, out AppEffect> {
            return when (action) {
                is AppReadyAction.ResetDayAction -> {
                    wrap(state, optDailyState, optStats) { oldDailyState, oldStats ->
                        if (state.resetStateEnabled) {
                            resetDay(
                                oldStats = oldStats,
                                newDailyState = action.defaultState,
                                oldDailyState = oldDailyState
                            )
                        } else {
                            (oldDailyState to oldStats).withEffects()
                        }
                    }
                }

                is AppReadyAction.FlipResetStateEnabledAction -> {
                    state.copy(resetStateEnabled = !state.resetStateEnabled).withEffects()
                }

                is OnResumeAction -> {
                    wrap(state, optDailyState, optStats) { oldDailyState, oldStats ->
                        if (action.datetime.isAfter(
                                LocalDateTime.of(
                                    oldDailyState.date.plusDays(1),
                                    LocalTime.of(5, 0)
                                )
                            )
                        ) {
                            resetDay(
                                oldStats = oldStats,
                                newDailyState = action.defaultDailyState,
                                oldDailyState = oldDailyState
                            )
                        } else {
                            (oldDailyState to oldStats).withEffects()
                        }
                    }
                }
            }
        }

        private fun resetDay(
            oldStats: Stats,
            oldDailyState: DailyState,
            newDailyState: DailyState,
        ): Reduced<Pair<DailyState, Stats>, AppEffect> {
            return (newDailyState to updateStats(oldStats, oldDailyState)).withEffects(
//                AddPromptEffect(RoutinePrompt(CleanTeeth))
            )
        }

        private fun updateStats(oldStats: Stats, oldDay: DailyState): Stats {
            return oldStats
        }
    }
}
