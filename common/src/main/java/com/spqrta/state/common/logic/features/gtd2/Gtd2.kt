package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.action.Gtd2ViewAction
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToCurrentState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToStats
import com.spqrta.state.common.logic.features.gtd2.logic.mapToTasksState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToTinderState
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.asOpticSet
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.set
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.tuple.Tuple4

typealias TinderTuple = Pair<TinderState, TasksDatabaseState>

object Gtd2 {

    /**
     * (DynalistState, TasksDatabaseState) -> TasksState
     * (CurrentState, TasksState, TasksDatabaseState) -> CurrentState
     * (TasksState, TasksDatabaseState) -> Gtd2Stats
     * (TinderTuple, TasksState) -> (TinderTuple)
     */

    private val optGtd2State = AppStateOptics.optReady + AppReadyOptics.optGtd2State
    private val optOnDynalistState =
        { state: Gtd2State, subState: Tuple4<TasksState, CurrentState, Gtd2Stats, TinderTuple> ->
            val (tasksState, currentState, stats, tinderTuple) = subState
            val (tinderState, tasksDatabase) = tinderTuple
            state.copy(
                currentState = currentState,
                stats = stats,
                tasksDatabase = tasksDatabase,
                tasksState = tasksState,
                tinderState = tinderState
            )
        }.asOpticSet()
    private val optOnTasksState =
        { state: Gtd2State, subState: Triple<CurrentState, Gtd2Stats, TinderTuple> ->
            val (currentState, stats, tinderTuple) = subState
            val (tinderState, tasksDatabase) = tinderTuple
            state.copy(
                currentState = currentState,
                stats = stats,
                tasksDatabase = tasksDatabase,
                tinderState = tinderState
            )
        }.asOpticSet()

    val viewReducer = widen(
        typeGet(),
        optGtd2State,
        ::viewReduce,
    )

    val reducer = widen(
        typeGet(),
        optGtd2State,
        ::reduce,
    )

    fun reduce(
        action: Gtd2Action,
        oldGtd2State: Gtd2State,
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is Gtd2Action.ToggleTask -> {
                updateTasksState(
                    oldGtd2State,
                    oldGtd2State.tasksState.withTaskToggled(action.task)
                )
            }

            is DebugAction.ResetDay -> {
                Gtd2State.initial().withEffects()
            }

            is Gtd2Action.DynalistStateUpdated -> {
                set(optOnDynalistState, oldGtd2State) {
                    val newTasksState = mapToTasksState(
                        action.dynalistState,
                        oldGtd2State.tasksDatabase
                    )
                    val currentState = mapToCurrentState(
                        oldGtd2State.currentState,
                        newTasksState,
                        oldGtd2State.tasksDatabase
                    )
                    val stats = mapToStats(
                        newTasksState,
                        oldGtd2State.tasksDatabase
                    )
                    val tinderTuple = mapToTinderState(
                        (oldGtd2State.tinderState to oldGtd2State.tasksDatabase),
                        newTasksState
                    ) to oldGtd2State.tasksDatabase

                    Tuple4(newTasksState, currentState, stats, tinderTuple)
                }.withEffects()
            }

            is Gtd2Action.OnTaskClick -> {
                updateTasksState(
                    oldGtd2State,
                    oldGtd2State.tasksState.withTaskClicked(action.task)
                )
            }

            is Gtd2Action.OnTaskLongClick -> {
                updateTasksState(
                    oldGtd2State,
                    oldGtd2State.tasksState.withTaskLongClicked(action.task)
                )
            }

            is StateLoadedAction -> {
                // we need to update timers and stuff on app load
                Gtd2State.optCurrent.set(
                    oldGtd2State,
                    mapToCurrentState(
                        oldGtd2State.currentState,
                        oldGtd2State.tasksState,
                        oldGtd2State.tasksDatabase
                    )
                ).withEffects()
            }
        }
    }

    fun viewReduce(
        action: Gtd2ViewAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return state.withEffects()
    }

    private fun updateTasksState(
        oldGtd2State: Gtd2State,
        newTasksState: TasksState,
    ): Reduced<Gtd2State, AppEffect> {
        return set(optOnTasksState, oldGtd2State) {
            val newCurrentState = mapToCurrentState(
                oldGtd2State.currentState,
                newTasksState,
                oldGtd2State.tasksDatabase
            )
            val stats = mapToStats(
                newTasksState,
                oldGtd2State.tasksDatabase
            )
            val tinderTuple = mapToTinderState(
                (oldGtd2State.tinderState to oldGtd2State.tasksDatabase),
                newTasksState
            ) to oldGtd2State.tasksDatabase

            Triple(newCurrentState, stats, tinderTuple)
        }.withEffects()
    }
}
