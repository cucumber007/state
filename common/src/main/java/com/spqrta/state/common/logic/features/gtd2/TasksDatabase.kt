package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.environments.tasks_database.TasksDatabaseEntry
import kotlinx.serialization.Serializable

@Serializable
data class TasksDatabaseState(
    val entries: Map<String, TasksDatabaseEntry>
) {
    fun withNewEntry(id: String, entry: TasksDatabaseEntry): TasksDatabaseState {
        return copy(entries = entries + (id to entry))
    }

    fun withUpdatedEntry(
        id: String,
        function: (TasksDatabaseEntry?) -> TasksDatabaseEntry
    ): TasksDatabaseState {
        return withNewEntry(id, function(entries[id]))
    }

    override fun toString(): String = "TasksDatabaseState(entries=${entries.size})"
}
