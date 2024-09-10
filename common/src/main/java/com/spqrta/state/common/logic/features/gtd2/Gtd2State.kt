package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.environments.tasks_database.DatabaseTask
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.features.gtd2.tinder.Tinder
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState
import com.spqrta.state.common.util.optics.asOptic
import kotlinx.serialization.Serializable

@Serializable
data class Gtd2State(
    val currentState: CurrentState,
    val metaState: MetaState,
    val taskTree: Element,
    val tasksDatabase: Map<String, DatabaseTask>,
    val tinderState: TinderState,
    val stats: Gtd2Stats = Gtd2Stats.INITIAL
) {
    companion object {
        val INITIAL_TASK_TREE = Task("Stub")

        val INITIAL = Gtd2State(
            currentState = CurrentState.INITIAL,
            metaState = MetaState(
                workdayStarted = false,
            ),
            taskTree = INITIAL_TASK_TREE,
            tasksDatabase = mapOf(),
            tinderState = TinderState.INITIAL,
        ).let {
            it.copy(tinderState = Tinder.getTinderState(it))
        }

        val optCurrent =
            ({ state: Gtd2State -> state.currentState } to { state: Gtd2State, subState: CurrentState ->
                state.copy(currentState = subState)
            }).asOptic()

        val optTaskTree =
            ({ state: Gtd2State -> state.taskTree } to { state: Gtd2State, subState: Element ->
                state.copy(taskTree = subState)
            }).asOptic()
    }
}
