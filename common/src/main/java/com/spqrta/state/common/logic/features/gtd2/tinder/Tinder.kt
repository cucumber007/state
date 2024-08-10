package com.spqrta.state.common.logic.features.gtd2.tinder

import com.spqrta.state.common.logic.action.AppReadyAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.action.TinderAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen

object Tinder {

    val reducer by lazy {
        widen(
            typeGet(),
            AppStateOptics.optReady + AppReadyOptics.optGtd2State,
            ::reduce
        )
    }

    private fun reduce(
        action: TinderAction,
        state: Gtd2State,
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is Gtd2Action.OnTaskClickAction,
            is Gtd2Action.OnTaskLongClickAction,
            is AppReadyAction.ResetDayAction -> Gtd2.reduce(action as Gtd2Action, state)

            else -> throw IllegalStateException("Unexpected action")
        }
    }

    fun getTinderState(state: Gtd2State): TinderState {
        val prompts = mutableListOf<TinderPrompt>()
        prompts.addAll(state.taskTree.nonEstimated().map {
            when (it) {
                is Task -> TinderPrompt.NonEstimatedTask(it)
                is Routine -> TinderPrompt.NonEstimatedRoutine(it)
                else -> throw IllegalStateException("Unexpected element type")
            }
        })
        return TinderState(prompts)
    }

}