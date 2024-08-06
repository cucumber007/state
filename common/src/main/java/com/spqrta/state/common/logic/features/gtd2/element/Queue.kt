package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    override val name: String,
    val elements: List<Element>,
    override val displayName: String = "$name Queue",
    override val active: Boolean = elements.isNotEmpty(),
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

    override fun withStatus(active: Boolean): Element {
        return copy(active = active)
    }
}