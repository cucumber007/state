package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Routine(
    override val active: Boolean = true,
    private val task: Task,
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