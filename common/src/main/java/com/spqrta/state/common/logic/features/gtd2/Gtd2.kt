package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.logic.action.AppReadyAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects

object Gtd2 {

    val reducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optGtd2State,
        ::reduce,
    )

    private fun reduce(
        action: Gtd2Action,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        val result: Reduced<out Gtd2State, out AppEffect> = when (action) {
            is Gtd2Action.OnTaskClickAction -> {
                val newState = state.copy(
                    taskTree = state.taskTree.withTaskClicked(action.task)
                )
                newState.withEffects()
            }

            is Gtd2Action.OnTaskLongClickAction -> {
                state.copy(
                    taskTree = state.taskTree.withTaskLongClicked(action.task)
                ).withEffects()
            }

            is AppReadyAction.ResetDayAction -> {
                Gtd2State.INITIAL.withEffects()
            }
        }
        return result.flatMapState {
            it.newState.copy(
                stats = reduceStats(
                    oldState = state,
                    newState = it.newState
                )
            )
        }
    }

    private fun reduceStats(
        oldState: Gtd2State,
        newState: Gtd2State
    ): Gtd2Stats {
        return oldState.stats.copy(a = oldState.stats.a + 1)
    }
}