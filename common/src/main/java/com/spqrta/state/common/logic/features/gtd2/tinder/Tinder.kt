package com.spqrta.state.common.logic.features.gtd2.tinder

import android.util.Log
import com.spqrta.state.common.environments.tasks_database.DatabaseTask
import com.spqrta.state.common.logic.action.Gtd2ViewAction
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
import com.spqrta.state.common.util.state_machine.withEffects

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
            // todo use Gtd2Action to update Tinder state?
            is Gtd2ViewAction.OnTaskClick,
            is Gtd2ViewAction.OnTaskLongClick -> Gtd2.reduceView(action as Gtd2ViewAction, state)

            is TinderAction.OnEstimated -> {
                state.copy(
                    taskTree = state.taskTree.withElement(action.element.name) {
                        it.withEstimate(action.element.name, action.estimate)
                    },
                    tasksDatabase = (state.tasksDatabase.get(action.element.name) ?: DatabaseTask(
                        action.element.name
                    )).let {
                        state.tasksDatabase.toMutableMap()
                            .also { map ->
                                map.put(
                                    action.element.name,
                                    it.copy(estimate = action.estimate)
                                )
                            }
                    }
                ).also {
                    Log.d("Tinder", "OnEstimated: $it")
                }.withEffects()
            }

            is TinderAction.OnSkipped -> {
                state.copy(
                    tinderState = state.tinderState.copy(
                        skipped = state.tinderState.skipped + action.prompt
                    )
                ).withEffects()
            }

            else -> throw IllegalStateException("Unexpected action: $action")
        }.flatMapState {
            it.newState.copy(
                tinderState = getTinderState(it.newState)
            )
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
        }.filter {
            !state.tinderState.skipped.contains(it)
        })
        return TinderState(prompts)
    }

}