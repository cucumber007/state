package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Routine(
    val active: Boolean = true,
    val task: Task,
    val trigger: RoutineTrigger = RoutineTrigger.Day(LocalDate.now()),
    override val name: String = task.name,
) : Element {
    override fun withTaskClicked(clickedTask: Task): Element {
        return task.withTaskClicked(clickedTask)
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return task.withTaskLongClicked(clickedTask)
    }
}