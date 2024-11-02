package com.spqrta.state.common.environments.tasks_database

import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

@Serializable
sealed class TasksDatabaseEntry {
    // and estimate for the task that exists in Dynalist
    @Serializable
    data class Estimate(
        val id: String,
        val estimate: TimeValue,
    ) : TasksDatabaseEntry()

    @Serializable
    data class Completed(
        val routine: Task,
    ) : TasksDatabaseEntry()
}