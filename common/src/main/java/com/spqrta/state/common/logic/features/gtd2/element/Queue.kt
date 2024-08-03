package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    override val name: String,
    val tasks: List<Element>,
) : Element {
    override fun withTaskClicked(task: Task): Element {
        return copy(tasks = tasks.map {
            it.withTaskClicked(task)
        })
    }

    override fun withTaskLongClicked(task: Task): Element {
        return copy(tasks = tasks.map {
            it.withTaskLongClicked(task)
        })
    }
}