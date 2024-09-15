package com.spqrta.state.common.logic.features.gtd2.logic

import com.spqrta.state.common.logic.features.gtd2.TasksDatabaseState
import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.current.TimeredState
import com.spqrta.state.common.logic.features.gtd2.current.TimeredTask
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus

fun mapToCurrentState(
    oldCurrentState: CurrentState,
    tasksState: TasksState,
    tasksDatabaseState: TasksDatabaseState
): CurrentState {
    return when (val activeElement = oldCurrentState.activeElement) {
        is ActiveElement.ActiveQueue -> {
            mapToCurrentStateActiveQueue(
                oldCurrentState,
                tasksState,
                activeElement
            )
        }

        null -> {
            mapToCurrentStateNoActiveElement(
                oldCurrentState,
                tasksState
            )
        }
    }
}

private fun mapToCurrentStateActiveQueue(
    oldCurrentState: CurrentState,
    tasksState: TasksState,
    activeElement: ActiveElement.ActiveQueue,
): CurrentState {
    val optActiveElement = CurrentState.optActiveElement
    val activeTask = activeElement.activeTask

    val newActiveQueue = tasksState.getElement(activeElement.queue.name) as Queue
    var newActiveElement = activeElement.copy(
        queue = newActiveQueue,
        activeTask = null
    )
    val newActiveTask = activeTask?.let {
        val newStateOfActiveTask =
            tasksState.getElement(activeTask.task.name) as Task
        when (newStateOfActiveTask.status) {
            is TaskStatus.Active -> {
                activeTask
            }

            is TaskStatus.Done, TaskStatus.Inactive -> {
                null
            }
        }
    } ?: run {
        // the previous active task was completed or there wasn't any
        if (newActiveElement.activeTasks.isNotEmpty()) {
            TimeredTask(
                newActiveElement.activeTasks.first(),
                TimeredState.Paused.INITIAL
            )
        } else {
            null
        }
    }
    newActiveElement = newActiveElement.copy(activeTask = newActiveTask)

    return optActiveElement.set(
        oldCurrentState,
        newActiveElement
    )
}

private fun mapToCurrentStateNoActiveElement(
    oldCurrentState: CurrentState,
    tasksState: TasksState,
): CurrentState {
    val optActiveElement = CurrentState.optActiveElement

    val queuesToChoose = tasksState.queues().filter { it.isLeafGroup() }
    val newActiveElement =
        queuesToChoose.let { queues ->
            if (queues.size == 1) {
                queues.first().let { queue ->
                    ActiveElement.ActiveQueue(
                        queue, queue.tasks().firstOrNull()?.let {
                            TimeredTask(
                                it,
                                TimeredState.Paused.INITIAL
                            )
                        }
                    )
                }
            } else {
                null
            }
        }
    return if (newActiveElement != null) {
        optActiveElement.set(oldCurrentState, newActiveElement)
    } else {
        oldCurrentState.copy(queuesToChoose = queuesToChoose)
    }
}
