package com.spqrta.state.common.logic.features.gtd2.logic

import android.util.Log
import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.R
import com.spqrta.state.common.logic.features.dynalist.DynalistLoadingState
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.gtd2.TasksDatabaseState
import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Flipper
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.flipper.FlipperSchedule
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineContext
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineTrigger
import com.spqrta.state.common.logic.features.gtd2.element.withTask
import com.spqrta.state.common.logic.features.gtd2.element.withToBeDone
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.result.Res
import com.spqrta.state.common.util.result.toNullable
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.common.util.result.tryRes

fun mapToTasksState(
    oldTasksState: TasksState,
    dynalistState: DynalistState,
    tasksDatabaseState: TasksDatabaseState,
    metaState: MetaState,
): TasksState {
    // todo get estimates from database
    return when (dynalistState) {
        is DynalistState.DocCreated -> {
            when (dynalistState.loadingState) {
                is DynalistLoadingState.Initial -> {
                    emptyQueue()
                }

                is DynalistLoadingState.Loaded -> {
                    dynalistState.loadingState.toElement().also {
                        println()
                    }
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
            metaState = metaState,
            newTasksState = newTasksState,
            oldTasksState = oldTasksState,
        )
    }
}

// migrate task statuses from old state to new state
private fun mergeTaskStates(
    metaState: MetaState,
    newTasksState: TasksState,
    oldTasksState: TasksState
): TasksState {
    var mergedTasksState = newTasksState
    newTasksState.toBeDone().forEach { task ->
        val oldTask = oldTasksState.toBeDone().find {
            it.name == task.name
        }
        if (oldTask != null) {
            // update statuses for the new task state to preserve tasks that are done etc.
            mergedTasksState = mergedTasksState.withToBeDone(task.name) {
                it.withStatus(oldTask.status)
            }
        }
    }
    return mergedTasksState.withNewContext(metaState)
}

private fun emptyQueue(): Queue {
    return Queue("No tasks")
}

private fun DynalistLoadingState.Loaded.toElement(): Element {
    return this.database.root.toElement()
}

fun DynalistNode.toElement(): Element {
    return if (this.children.isNotEmpty()) {
        val note = this.note?.let { parseNote(it) }?.toNullable()
        when {
            note?.flipper == true -> {
                Flipper(
                    name = this.title,
                    scheduledElements = this.children.map { dynalistNode ->
                        val subtaskNote = dynalistNode.note?.let { parseNote(it) }?.toNullable()
                        FlipperSchedule.parse(subtaskNote?.schedule ?: "", dynalistNode.toElement())
                            ?: FlipperSchedule.Just(dynalistNode.toElement())
                    }
                )
            }

            else -> {
                Queue(
                    name = this.title,
                    elements = this.children.map { it.toElement() }
                )
            }
        }
    } else {
        val note = this.note?.let { parseNote(it) }?.toNullable()
        when {
            note?.trigger != null -> {
                Routine(
                    element = Task(
                        name = this.title,
                        estimate = note.estimate?.toMinutes()
                    ),
                    trigger = note.trigger
                )
            }

            else -> {
                Task(
                    name = this.title,
                    estimate = note?.estimate?.toMinutes()
                )
            }
        }
    }.also {
        println(it)
    }
}

private fun parseNote(note: String): Res<DynalistNoteParams> {
    return tryRes {
        try {
            mapOf(
                DynalistNoteParams.KEY_ESTIMATE to note.toInt().toString()
            )
        } catch (e: NumberFormatException) {
            note.split(",").map { paramString ->
                paramString.split("=").let {
                    it.first() to it[1]
                }
            }.toMap()
        }
    }.flatMapSuccess {
        DynalistNoteParams.parse(it)
    }
}

private fun String?.parseEstimate(): Int? {
    return this?.toIntOrNull()
}
