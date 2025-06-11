package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.dynalist.utility.pure.Optional
import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.element.Group
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.ToBeDone
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.util.optics.asOpticGet
import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
sealed class ActiveElement {
    @Serializable
    data class ActiveQueue(
        val queue: ElementName,
        val activeTask: Optional<TimeredTask>
    ) : ActiveElement() {

        fun activeTasksValue(tasksState: TasksState): List<ToBeDone> {
            return groupValue(tasksState).toBeDone().also {
                println("ActiveTasksValue: $it")
            }.filter { it.active }
        }

        fun groupValue(tasksState: TasksState): Group {
            return tasksState.groups().find { it.name == queue } ?: Queue("Error")
        }

        companion object {
            fun optFirstTask(tasksState: TasksState) = { state: ActiveQueue ->
                state.groupValue(tasksState).tasks().firstOrNull()
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
