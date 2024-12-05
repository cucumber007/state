package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.environments.tasks_database.TasksDatabaseEntry
import com.spqrta.state.common.util.serialization.LocalDateSerializer
import com.spqrta.state.common.util.toIso
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class TasksDatabaseState(
    val estimates: Map<String, TasksDatabaseEntry.Estimate>,
    val completed: List<TasksDatabaseEntry.Completed>,
) {
    // todo optimize
    fun tasksLastCompleted(): Map<String, LocalDate> {
        val result = mutableMapOf<String, LocalDate>()
        completed.map { it.routine.name }.toSet().forEach {
            val max = completed.filter {
                it.routine.name == it.routine.name
            }.maxByOrNull {
                it.dateTime.toIso()
            }

//            val a: LocalDate? = max?.dateTime?.toLocalDate()
            val a: LocalDate? = LocalDate.now()

            result.put(it.value, a!!)
        }
        return result
    }

    fun withNewEntry(id: String, entry: TasksDatabaseEntry.Completed): TasksDatabaseState {
        return copy(
            completed = completed + entry
        )
    }

    fun withUpdatedEntry(
        id: String,
        function: (TasksDatabaseEntry.Estimate?) -> TasksDatabaseEntry.Estimate
    ): TasksDatabaseState {
        return copy(
            estimates = estimates.toMutableMap().apply {
                put(id, function(get(id)))
            }
        )
    }

    override fun toString(): String = listOf(
        "estimates: ${estimates.size}",
        "completed: ${completed.size}",
    ).joinToString(",").let { "${javaClass.simpleName}(it)" }
}
