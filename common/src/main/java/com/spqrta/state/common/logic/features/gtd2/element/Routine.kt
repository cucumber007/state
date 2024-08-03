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
    override fun withTaskClicked(task: Task): Element {
        return task.withTaskClicked(task)
    }

    override fun withTaskLongClicked(task: Task): Element {
        return task.withTaskLongClicked(task)
    }
}