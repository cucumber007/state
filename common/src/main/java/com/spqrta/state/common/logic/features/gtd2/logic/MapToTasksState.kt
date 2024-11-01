package com.spqrta.state.common.logic.features.gtd2.logic

import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.logic.features.dynalist.DynalistLoadingState
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.gtd2.TasksDatabaseState
import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.time.toMinutes

fun mapToTasksState(
    oldTasksState: TasksState,
    dynalistState: DynalistState,
    tasksDatabaseState: TasksDatabaseState
): TasksState {
    // todo get estimates from database
    return when (dynalistState) {
        is DynalistState.DocCreated -> {
            when (dynalistState.loadingState) {
                is DynalistLoadingState.Initial -> {
                    emptyQueue()
                }

                is DynalistLoadingState.Loaded -> {
                    dynalistState.loadingState.toElement()
                }
            }
        }

        is DynalistState.CreatingDoc,
        is DynalistState.DocsLoading,
        is DynalistState.KeyNotSet -> {
            emptyQueue()
        }
    }.let { newTasksState: TasksState ->
        mergeTaskStates(
            newTasksState = newTasksState,
            oldTasksState = oldTasksState
        )
    }
}

private fun mergeTaskStates(
    newTasksState: TasksState,
    oldTasksState: TasksState
): TasksState {
    var mergedTasksState = newTasksState
    newTasksState.tasks().forEach { task ->
        val oldTask = oldTasksState.tasks().find { it.name == task.name }
        if (oldTask != null) {
            mergedTasksState = mergedTasksState.withElement(task.name) {
                it.withTaskStatus(oldTask.status)
            }
        }
    }
    return mergedTasksState
}

private fun emptyQueue(): Queue {
    return Queue("No tasks")
}

private fun DynalistLoadingState.Loaded.toElement(): Element {
    return Queue(
        name = "Main",
        elements = this.database.root.children.map { it.toElement() }
    )
}

fun DynalistNode.toElement(): Element {
    return if (this.children.isNotEmpty()) {
        Queue(
            name = this.title,
            elements = this.children.map { it.toElement() }
        )
    } else {
        Task(
            name = this.title,
            estimate = this.note.parseEstimate()?.toMinutes()
        )
    }
}

private fun String?.parseEstimate(): Int? {
    return this?.toIntOrNull()
}
