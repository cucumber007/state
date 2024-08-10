package com.spqrta.state.common.logic.features.stats

import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.StatsAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.time.PositiveSeconds
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import java.time.LocalTime

object Stats {
    val reducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optGtd2State,
        ::reduce
    )

    private fun reduce(
        action: StatsAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is ClockAction.TickAction -> {
                state.copy(
                    stats = reduceStats(state, state)
                ).withEffects()
            }

            is StatsAction.UpdateStatsAction -> {
                state.copy(
                    stats = reduceStats(action.oldState, state)
                ).withEffects()
            }
        }
    }

    private fun reduceStats(
        oldState: Gtd2State,
        newState: Gtd2State
    ): Gtd2Stats {
        return oldState.stats.copy(
            timeLeft = calculateTimeLeft(),
            estimate = newState.taskTree.estimate() ?: 0.toSeconds()
        )
    }

    private fun calculateTimeLeft(): TimeValue {
        return (LocalTime.MAX.toSecondOfDay() - LocalTime.now()
            .toSecondOfDay()).let { PositiveSeconds(it) }
    }
}
