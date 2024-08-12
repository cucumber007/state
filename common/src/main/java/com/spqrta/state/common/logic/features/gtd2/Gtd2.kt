package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.logic.action.AppReadyAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.action.Gtd2ViewAction
import com.spqrta.state.common.logic.action.StatsAction
import com.spqrta.state.common.logic.effect.ActionEffect
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.tinder.Tinder
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects

object Gtd2 {

    val viewReducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optGtd2State,
        ::reduceView,
    )

    val reducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optGtd2State,
        ::reduce,
    )

    fun reduceView(
        action: Gtd2ViewAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is Gtd2ViewAction.OnTaskClick -> {
                state.copy(
                    taskTree = state.taskTree.withTaskClicked(action.task),
                ).withEffects<Gtd2State, AppEffect>()
            }

            is Gtd2ViewAction.OnTaskLongClick -> {
                state.copy(
                    taskTree = state.taskTree.withTaskLongClicked(action.task)
                ).withEffects()
            }

            is AppReadyAction.ResetDayAction -> {
                Gtd2State.INITIAL.withEffects()
            }
        }.flatMapEffects {
            it.effects + ActionEffect(
                StatsAction.UpdateStatsAction(
                    newState = it.newState,
                    oldState = state
                )
            )
        }.flatMapState {
            it.newState.copy(
                tinderState = Tinder.getTinderState(it.newState)
            )
        }
    }

    fun reduce(
        action: Gtd2Action,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is Gtd2Action.ToggleTask -> {
                state.copy(
                    taskTree = state.taskTree.withTaskToggled(action.task)
                ).withEffects()
            }
        }
    }
}