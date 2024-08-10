package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    override val name: String,
    val elements: List<Element>,
    override val displayName: String = "$name Queue",
    override val active: Boolean = elements.isNotEmpty(),
) : Element {

    @Suppress("RedundantNullableReturnType")
    override fun estimate(): TimeValue? {
        return elements.sumOf {
            it.estimate()?.seconds ?: 0
        }.toSeconds()
    }

    override fun nonEstimated(): List<Element> {
        return elements.map {
            it.nonEstimated()
        }.flatten()
    }

    override fun withElement(name: String, action: (element: Element) -> Element): Element {
        return copy(elements = elements.map {
            it.withElement(name, action)
        })
    }

    override fun withEstimate(name: String, estimate: TimeValue?): Element {
        return copy(elements = elements.map {
            it.withEstimate(name, estimate)
        })
    }

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