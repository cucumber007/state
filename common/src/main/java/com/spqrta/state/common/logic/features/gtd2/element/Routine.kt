package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.RoutineTrigger
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Routine(
    private val task: Task,
    override val displayName: String = "${task.name} Routine",
    override val active: Boolean = true,
    val trigger: RoutineTrigger = RoutineTrigger.Day(LocalDate.now()),
    override val name: String = task.name,
) : Element {

    val normalizedTask = task.withStatus(
        if (!active) {
            TaskStatus.Inactive
        } else {
            task.status
        }
    )

    override fun withTaskClicked(clickedTask: Task): Element {
        return normalizedTask.withTaskClicked(clickedTask)
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return normalizedTask.withTaskLongClicked(clickedTask)
    }
}