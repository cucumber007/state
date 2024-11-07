package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.dynalyst.utility.pure.Optional
import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.ToBeDone
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.optics.asOpticGet
import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
sealed class ActiveElement {
    @Serializable
    data class ActiveQueue(
        val queue: ElementName.QueueName,
        val activeTask: Optional<TimeredTask>
    ) : ActiveElement() {

        fun activeTasksValue(tasksState: TasksState): List<ToBeDone> {
            return queueValue(tasksState).toBeDone().filter { it.active }
        }

        fun queueValue(tasksState: TasksState): Queue {
            return tasksState.queues().find { it.name == queue } ?: Queue("Error")
        }

        companion object {
            fun optFirstTask(tasksState: TasksState) = { state: ActiveQueue ->
                state.queueValue(tasksState).tasks().firstOrNull()
            }.asOpticGet()
        }
    }


    companion object {
        val optActiveTask = ({ state: ActiveElement ->
            when (state) {
                is ActiveQueue -> state.activeTask
            }
        } to { state: ActiveElement, subState: Optional<TimeredTask> ->
            @Suppress("USELESS_CAST")
            when (state) {
                is ActiveQueue -> state.copy(
                    activeTask = subState
                )
            } as ActiveElement
        }).asOpticOptional()
    }
}
