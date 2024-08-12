package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.environments.tasks_database.DatabaseTask
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.features.gtd2.tinder.Tinder
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState
import kotlinx.serialization.Serializable

@Serializable
data class Gtd2State(
    val metaState: MetaState,
    val taskTree: Element,
    val tasksDatabase: Map<String, DatabaseTask>,
    val tinderState: TinderState,
    val stats: Gtd2Stats = Gtd2Stats.INITIAL
) {
    companion object {
        private val INITIAL_TASK_TREE = Task("No tasks")

        val INITIAL = Gtd2State(
            MetaState(
                workdayStarted = false,
            ),
            taskTree = INITIAL_TASK_TREE,
            tinderState = TinderState.INITIAL,
            tasksDatabase = mapOf()
        ).let {
            it.copy(tinderState = Tinder.getTinderState(it))
        }
    }
}