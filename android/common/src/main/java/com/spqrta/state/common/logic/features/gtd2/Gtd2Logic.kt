@file:Suppress("SimpleRedundantLet")

package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.util.log.Logg
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToCurrentState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToStats
import com.spqrta.state.common.logic.features.gtd2.logic.mapToTasksState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToTinderState
import com.spqrta.state.common.logic.features.gtd2.logic.mergeTaskStates
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState
import com.spqrta.state.common.util.debug.debugAlsoFirstTask
import com.spqrta.state.common.util.tuple.Tuple4

/**
 * (Changed State) -> (Affected States):
 *
 * (Meta) -> (Tasks)
 * (Dynalist) -> (TasksDatabase, Tasks)
 * (TasksDatabase) -> (Tasks, Current, GtdStats, Tinder) + Meta
 * (Tasks) -> (Current, GtdStats, Tinder)
 * (Current) -> (GtdStats)
 * (GtdStats) -> ()
 * (Tinder) -> ()
 */

fun updateMetaWithDeps(
    oldParentState: Gtd2State,
    newMetaState: MetaState,
    dynalistState: DynalistState,
): Gtd2State {
    return updateMetaStateSyncronized(
        oldParentState,
        newMetaState,
        dynalistState
    ) { newParentState, newTasksState ->
        updateTasksWithDeps(
            newParentState,
            newTasksState
        )
    }
}

fun updateMetaStateSyncronized(
    parentState: Gtd2State,
    updatedState: MetaState,
    dynalistState: DynalistState,
    updateDeps: (Gtd2State, TasksState) -> Gtd2State,
): Gtd2State {
    return parentState.copy(metaState = updatedState).let { newParentState ->
        val newTasksState = mapToTasksState(
            newParentState.tasksState,
            dynalistState,
            newParentState.tasksDatabase,
            newParentState.metaState
        )
        updateDeps(newParentState, newTasksState)
    }
}

fun updateDynalistWithDeps(
    oldParentState: Gtd2State,
    dynalistState: DynalistState
): Gtd2State {
    return updateDynalistStateSyncronized(
        oldParentState,
        dynalistState
    ) { newParentState, (newTasksState, newTasksDatabaseState) ->
        updateTasksWithDeps(
            newParentState,
            newTasksState
        ).let { newParentState ->
            updateTasksDatabaseWithDeps(
                newParentState,
                newTasksDatabaseState,
                dynalistState,
            )
        }
    }
}

fun updateDynalistStateSyncronized(
    parentState: Gtd2State,
    updatedState: DynalistState,
    updateDeps: (Gtd2State, Pair<TasksState, TasksDatabaseState>) -> Gtd2State,
): Gtd2State {
    return parentState.let {
        val newTasksState = mapToTasksState(
            it.tasksState,
            updatedState,
            it.tasksDatabase,
            it.metaState
        )
        val newTasksDatabaseState = it.tasksDatabase
        updateDeps(parentState, newTasksState to newTasksDatabaseState)
    }
}

fun updateTasksStateSyncronized(
    parentState: Gtd2State,
    updatedState: TasksState,
    updateDeps: (Gtd2State, Triple<TinderState, Gtd2Stats, CurrentState>) -> Gtd2State,
): Gtd2State {
    return parentState.copy(tasksState = updatedState).let { newParentState ->
        val newCurrentState = mapToCurrentState(
            newParentState.currentState,
            newParentState.tasksState,
            newParentState.tasksDatabase
        )
        val newTinderState = mapToTinderState(
            newParentState.tinderState to newParentState.tasksDatabase,
            newParentState.tasksState
        )
        val newStats = mapToStats(
            newParentState.tasksState,
            newParentState.tasksDatabase,
        )
        updateDeps(newParentState, Triple(newTinderState, newStats, newCurrentState))
    }
}

fun updateTasksWithDeps(
    newParentState: Gtd2State,
    newTasksStateUnmerged: TasksState,
): Gtd2State {
    // to update context on task update
    val newTasksState = mergeTaskStates(
        metaState = newParentState.metaState,
        newTasksState = newTasksStateUnmerged,
        oldTasksState = newParentState.tasksState,
    )
    return updateTasksStateSyncronized(
        newParentState,
        newTasksState
    ) { newParentState, (newTinderState, newStats, newCurrentState) ->
        updateTinderStateWithDeps(
            newParentState,
            newTinderState,
        ).let { newParentState ->
            updateStatsWithDeps(
                newParentState,
                newStats,
            )
        }.let { newParentState ->
            updateCurrentWithDeps(
                newParentState,
                newCurrentState
            )
        }
    }
}

private fun updateTasksDatabaseStateSyncronized(
    parentState: Gtd2State,
    updatedState: TasksDatabaseState,
    dynalistState: DynalistState,
    updateDeps: (Gtd2State, Tuple4<TasksState, CurrentState, TinderState, Gtd2Stats>) -> Gtd2State,
): Gtd2State {
    return parentState.copy(tasksDatabase = updatedState).let { newParentState ->
        val newTasksState = mapToTasksState(
            newParentState.tasksState,
            dynalistState,
            newParentState.tasksDatabase,
            newParentState.metaState
        )
        val newCurrentState = mapToCurrentState(
            newParentState.currentState,
            newTasksState,
            newParentState.tasksDatabase
        )
        val newTinderState = mapToTinderState(
            newParentState.tinderState to newParentState.tasksDatabase,
            newTasksState
        )
        val newStats = mapToStats(
            newTasksState,
            newParentState.tasksDatabase,
        )
        updateDeps(newParentState, Tuple4(newTasksState, newCurrentState, newTinderState, newStats))
    }
}

fun updateTasksDatabaseWithDeps(
    newParentState: Gtd2State,
    newTasksDatabaseState: TasksDatabaseState,
    dynalistState: DynalistState,
): Gtd2State {
    return updateTasksDatabaseStateSyncronized(
        newParentState,
        newTasksDatabaseState,
        dynalistState
    ) { newParentState, (newTasksState, newCurrentState, newTinderState, newStats) ->
        updateTasksWithDeps(
            newParentState,
            newTasksState
        ).let {
            updateCurrentWithDeps(
                newParentState,
                newCurrentState,
            )
        }.let {
            updateTinderStateWithDeps(
                it,
                newTinderState
            )
        }.let {
            updateStatsWithDeps(
                it,
                newStats
            )
        }
    }.let {
        it.copy(
            metaState = it.metaState.withUpdatedTasksDatabase(newTasksDatabaseState)
        )
    }
}

fun updateTinderStateWithDeps(
    parentState: Gtd2State,
    updatedState: TinderState,
): Gtd2State {
    return parentState.copy(tinderState = updatedState)
}

fun updateStatsWithDeps(
    parentState: Gtd2State,
    updatedState: Gtd2Stats,
): Gtd2State {
    return parentState.copy(stats = updatedState)
}

private fun updateCurrentStateSyncronized(
    parentState: Gtd2State,
    updatedState: CurrentState,
    updateDeps: (Gtd2State, Gtd2Stats) -> Gtd2State,
): Gtd2State {
    return parentState.copy(currentState = updatedState).let { newParentState ->
        val newStats = mapToStats(
            newParentState.tasksState,
            newParentState.tasksDatabase,
        )
        updateDeps(newParentState, newStats)
    }
}

fun updateCurrentWithDeps(
    newParentState: Gtd2State,
    newCurrentState: CurrentState,
): Gtd2State {
    return updateCurrentStateSyncronized(
        newParentState,
        newCurrentState
    ) { newParentState, newStats ->
        updateStatsWithDeps(
            newParentState,
            newStats
        )
    }
}
