package com.spqrta.state.common.logic.features.gtd2.element

import android.util.Log
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    override val name: ElementName.QueueName,
    val elements: List<Element> = listOf(),
    override val displayName: String = "$name Queue",
    override val active: Boolean = elements.isNotEmpty(),
) : Element, Group {
    constructor(name: String, elements: List<Element>) : this(ElementName.QueueName(name), elements)
    constructor(name: String) : this(ElementName.QueueName(name))

    @Suppress("RedundantNullableReturnType")
    override fun estimate(): TimeValue? {
        val sum = elements.sumOf {
            val estimate = it.estimate()
            estimate?.totalSeconds ?: 0
        }
        return sum.toSeconds()
    }

    override fun getElement(name: ElementName): Element? {
        return if (this.name == name) {
            this
        } else {
            elements.firstNotNullOfOrNull { it.getElement(name) }
        }
    }

    override fun getToBeDone(name: ElementName): ToBeDone? {
        return elements.firstNotNullOfOrNull { it.getToBeDone(name) }
    }

    override fun isLeaf(): Boolean {
        return false
    }

    override fun isLeafGroup(): Boolean {
        return elements.all {
            it.isLeaf()
        }
    }

    override fun map(mapper: (Element) -> Element): Element {
        return copy(elements = elements.map {
            it.map(mapper)
        })
    }

    override fun groups(): List<Group> {
        return elements.map {
            it.groups()
        }.flatten()
    }

    override fun nonEstimated(): List<Element> {
        return elements.map {
            it.nonEstimated()
        }.flatten()
    }

    override fun tasks(): List<Task> {
        return elements.map {
            it.tasks()
        }.flatten()
    }

    override fun toBeDone(): List<ToBeDone> {
        return elements.map {
            it.toBeDone()
        }.flatten()
    }

    override fun withDoneReset(): Element {
        return copy(elements = elements.map {
            it.withDoneReset()
        })
    }

    override fun withElement(name: ElementName, action: (element: Element) -> Element): Element {
        return copy(elements = elements.map {
            it.withElement(name, action)
        })
    }

    override fun withEstimate(name: ElementName, estimate: TimeValue?): Element {
        return copy(elements = elements.map {
            it.withEstimate(name, estimate)
        })
    }

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }

    override fun withNewContext(metaState: MetaState): Element {
        return copy(
            elements = elements.map {
                it.withNewContext(metaState)
            }
        )
    }
}
