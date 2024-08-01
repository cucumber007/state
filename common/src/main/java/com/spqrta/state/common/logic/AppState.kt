package com.spqrta.state.common.logic

import com.spqrta.state.common.logic.action.AppReadyAction
import com.spqrta.state.common.logic.action.OnResumeAction
import com.spqrta.state.common.logic.features.alarms.AlarmsState
import com.spqrta.state.common.logic.features.daily.DailyState
import com.spqrta.state.common.logic.features.daily.clock_mode.ClockMode
import com.spqrta.state.common.logic.features.daily.routine.CleanTeeth
import com.spqrta.state.common.logic.features.daily.timers.Timers
import com.spqrta.state.common.logic.features.frame.FrameState
import com.spqrta.state.common.logic.features.global.AddPromptEffect
import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.logic.features.global.AppGlobalState
import com.spqrta.state.common.logic.features.stats.Stats
import com.spqrta.state.common.logic.features.storage.Storage
import com.spqrta.state.common.logic.optics.AppReadyOptics.optDailyState
import com.spqrta.state.common.logic.optics.AppReadyOptics.optStats
import com.spqrta.state.common.util.optics.Optic
import com.spqrta.state.common.util.optics.asOptic
import com.spqrta.state.common.util.optics.identityOptional
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.optics.wrap
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalTime

sealed class AppState {
    override fun toString(): String = javaClass.simpleName
}

object AppNotInitialized : AppState()

@Serializable
data class AppReady(
    @Contextual val timers: Timers = Timers(),
    val alarmsState: AlarmsState = AlarmsState(),
    val clockMode: ClockMode = ClockMode.INITIAL,
    val frameState: FrameState = FrameState.INITIAL,
    val globalState: AppGlobalState = AppGlobalState(),
    val resetStateEnabled: Boolean = false,
    val stats: Stats = Stats(),
    val storage: Storage = Storage(),
) : AppState() {

    companion object {
        val INITIAL = AppReady()

        val reducer = widen(
            typeGet(),
            identityOptional(),
            Companion::reduce,
        )

        private fun reduce(
            action: AppReadyAction,
            state: AppReady
        ): Reduced<out AppReady, out AppEffect> {
            return when (action) {
                AppReadyAction.ResetDayAction -> {
                    wrap(state, optDailyState, optStats) { oldDailyState, oldStats ->
                        if (state.resetStateEnabled) {
                            resetDay(oldStats, oldDailyState)
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
                            resetDay(oldStats, oldDailyState)
                        } else {
                            (oldDailyState to oldStats).withEffects()
                        }
                    }
                }
            }
        }

        private fun resetDay(
            oldStats: Stats,
            oldDailyState: DailyState
        ): Reduced<Pair<DailyState, Stats>, AppEffect> {
            return (DailyState.INITIAL to updateStats(oldStats, oldDailyState)).withEffects(
                AddPromptEffect(RoutinePrompt(CleanTeeth))
            )
        }

        private fun updateStats(oldStats: Stats, oldDay: DailyState): Stats {
            return oldStats
        }

        val optAlarmsState: Optic<AppReady, AlarmsState> = ({ state: AppReady ->
            state.alarmsState
        } to { state: AppReady, subState: AlarmsState ->
            state.copy(alarmsState = subState)
        }).asOptic()
    }
}
