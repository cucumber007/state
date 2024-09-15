package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.environments.tasks_database.DatabaseTask
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.logic.mapToCurrentState
import com.spqrta.state.common.logic.features.gtd2.logic.mapToTinderState
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState
import com.spqrta.state.common.util.optics.asOptic
import kotlinx.serialization.Serializable

typealias TasksDatabaseState = Map<String, DatabaseTask>
typealias TasksState = Element

@Serializable
data class Gtd2State(
    val currentState: CurrentState,
    val metaState: MetaState,
    val tasksState: TasksState,
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

        val optTaskTree =
            ({ state: Gtd2State -> state.tasksState } to { state: Gtd2State, subState: Element ->
                state.copy(tasksState = subState)
            }).asOptic()

        val optTinder =
            ({ state: Gtd2State -> state.tinderState } to { state: Gtd2State, subState: TinderState ->
                state.copy(tinderState = subState)
            }).asOptic()

        private val INITIAL_TASK_TREE = Task("Stub")
        private val INITIAL = Gtd2State(
            currentState = CurrentState.INITIAL,
            metaState = MetaState(
                workdayStarted = false,
            ),
            tasksState = INITIAL_TASK_TREE,
            tasksDatabase = mapOf(),
            tinderState = TinderState.INITIAL,
        )
    }
}
