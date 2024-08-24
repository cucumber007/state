package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.optics.asOpticGet
import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
sealed class ActiveElement {
    @Serializable
    data class ActiveQueue(
        val queue: Queue,
        val activeTask: TimeredTask?
    ) : ActiveElement() {
        val tasksToShow: List<Task> = queue.tasks()

        // .filter { it.status == TaskStatus.Active }
        val activeTasks: List<Task> = queue.tasks().filter { it.status == TaskStatus.Active }

        companion object {
            val optFirstTask = { state: ActiveQueue ->
                state.queue.tasks().firstOrNull()
            }.asOpticGet()
        }
    }


    companion object {
        val optActiveTask = ({ state: ActiveElement ->
            when (state) {
                is ActiveQueue -> state.activeTask
            }
        } to { state: ActiveElement, subState: TimeredTask? ->
            when (state) {
                is ActiveQueue -> state.copy(
                    activeTask = subState
                )
            } as ActiveElement
        }).asOpticOptional()
    }
}
