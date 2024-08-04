package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    override val name: String,
    val elements: List<Element>,
    override val active: Boolean = true,
) : Element {
    override fun withTaskClicked(clickedTask: Task): Element {
        return copy(elements = elements.map {
            it.withTaskClicked(clickedTask)
        })
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return copy(elements = elements.map {
            it.withTaskLongClicked(clickedTask)
        })
    }
}