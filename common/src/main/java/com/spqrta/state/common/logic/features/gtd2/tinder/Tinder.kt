package com.spqrta.state.common.logic.features.gtd2.tinder

import com.spqrta.state.common.environments.tasks_database.TasksDatabaseEntry
import com.spqrta.state.common.logic.action.TinderAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.TinderTuple
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToCurrentState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToStats
import com.spqrta.state.common.logic.features.gtd2.logic.mapToTasksState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToTinderState
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.asOpticSet
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.times
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.set
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.tuple.Tuple4

object Tinder {

    private val optDynalistState = AppStateOptics.optReady + AppReadyOptics.optDynalistState
    private val optGtd2State = AppStateOptics.optReady + AppReadyOptics.optGtd2State
    private val optOnTasksDatabase =
        { state: Gtd2State, subState: Tuple4<TasksState, CurrentState, Gtd2Stats, TinderTuple> ->
            val (tasksState, currentState, stats, tinderTuple) = subState
            val (tinderState, tasksDatabase) = tinderTuple
            state.copy(
                tasksState = tasksState,
                currentState = currentState,
                stats = stats,
                tinderState = tinderState,
                tasksDatabase = tasksDatabase
            )
        }.asOpticSet()

    val reducer by lazy {
        widen(
            typeGet(),
            optGtd2State * optDynalistState,
            optGtd2State,
            ::reduce
        )
    }

    private fun reduce(
        action: TinderAction,
        state: Pair<Gtd2State, DynalistState>,
    ): Reduced<out Gtd2State, out AppEffect> {
        val (gtd2State, dynalistState) = state
        return when (action) {
            is TinderAction.OnEstimated -> {
                val newTasksDatabaseState = gtd2State.tasksDatabase.toMutableMap().also { map ->
                    map.put(
                        action.element.name.value,
                        TasksDatabaseEntry.Estimate(
                            action.element.name.value,
                            action.estimate
                        )
                    )
                }
                updateTasksDatabase(state, newTasksDatabaseState)
            }

            is TinderAction.OnSkipped -> {
                Gtd2State.optTinder.set(
                    gtd2State,
                    gtd2State.tinderState.copy(
                        skipped = gtd2State.tinderState.skipped + action.prompt
                    )
                ).withEffects()
            }

            is TinderAction.OnMetaState -> {
                Gtd2State.optMeta.set(
                    gtd2State,
                    gtd2State.metaState.set(action.property, action.value)
                ).withEffects()
            }
        }
    }

    private fun updateTasksDatabase(
        state: Pair<Gtd2State, DynalistState>,
        newTasksDatabaseState: Map<String, TasksDatabaseEntry>
    ): Reduced<out Gtd2State, out AppEffect> {
        val (gtd2State, dynalistState) = state
        return set(optOnTasksDatabase, gtd2State) {
            val newTasksState = mapToTasksState(
                gtd2State.tasksState,
                dynalistState,
                newTasksDatabaseState,
                gtd2State.metaState
            )
            val currentState =
                mapToCurrentState(gtd2State.currentState, newTasksState, newTasksDatabaseState)
            val stats = mapToStats(newTasksState, newTasksDatabaseState)
            val tinderTuple = mapToTinderState(
                (gtd2State.tinderState to newTasksDatabaseState),
                newTasksState
            ) to newTasksDatabaseState
            Tuple4(newTasksState, currentState, stats, tinderTuple)
        }.withEffects()
    }

}
