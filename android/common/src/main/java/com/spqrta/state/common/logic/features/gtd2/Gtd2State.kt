package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.logic.mapToCurrentState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToTinderState
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState
import com.spqrta.state.common.util.optics.asOptic
import com.spqrta.state.common.util.optics.asOpticGet
import kotlinx.serialization.Serializable

typealias TasksState = Element

@Serializable
data class Gtd2State(
    val currentState: CurrentState,
    val metaState: MetaState,
    val tasksState: TasksState,
    /**
     * TasksDatabase is a local storage of task status and estimates, to avoid mutating Dynalist
     * state. It also contains completed history from Dynalist.
     */
    val tasksDatabase: TasksDatabaseState,
    val tinderState: TinderState,
    val stats: Gtd2Stats = Gtd2Stats.INITIAL
) {
    companion object {

        fun initial() = INITIAL.let {
            optCurrent.set(
                it,
                mapToCurrentState(
                    it.currentState,
                    it.tasksState,
                    it.tasksDatabase
                )
            )
            optTinder.set(
                it,
                mapToTinderState(
                    it.tinderState to it.tasksDatabase,
                    it.tasksState
                )
            )
        }

        val optCurrent =
            ({ state: Gtd2State -> state.currentState } to { state: Gtd2State, subState: CurrentState ->
                state.copy(currentState = subState)
            }).asOptic()
        val optFirstTask = { state: Gtd2State ->
            state.currentState.activeElement?.let {
                when (it) {
                    is ActiveElement.ActiveQueue -> it.groupValue(state.tasksState).toBeDone()
                        .firstOrNull()
                }
            }
        }.asOpticGet()
        val optTasksDatabase =
            ({ state: Gtd2State -> state.tasksDatabase } to { state: Gtd2State, subState: TasksDatabaseState ->
                state.copy(tasksDatabase = subState)
            }).asOptic()
        val optTasks =
            ({ state: Gtd2State -> state.tasksState } to { state: Gtd2State, subState: Element ->
                state.copy(tasksState = subState)
            }).asOptic()

        val optTinder =
            ({ state: Gtd2State -> state.tinderState } to { state: Gtd2State, subState: TinderState ->
                state.copy(tinderState = subState)
            }).asOptic()

        val optMeta =
            ({ state: Gtd2State -> state.metaState } to { state: Gtd2State, subState: MetaState ->
                state.copy(metaState = subState)
            }).asOptic()

        private val INITIAL_TASK_TREE = Task("Stub")
        private val INITIAL = Gtd2State(
            currentState = CurrentState.INITIAL,
            metaState = MetaState.INITIAL,
            tasksState = INITIAL_TASK_TREE,
            tasksDatabase = TasksDatabaseState(
                estimates = emptyMap(),
                completed = emptyList(),
            ),
            tinderState = TinderState.INITIAL,
        )
    }
}
