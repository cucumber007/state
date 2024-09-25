package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    override val name: ElementName.QueueName,
    val elements: List<Element> = listOf(),
    override val displayName: String = "$name Queue",
    override val active: Boolean = elements.isNotEmpty(),
) : Element {
    constructor(name: String, elements: List<Element>) : this(ElementName.QueueName(name), elements)
    constructor(name: String) : this(ElementName.QueueName(name))

    @Suppress("RedundantNullableReturnType")
    override fun estimate(): TimeValue? {
//        Log.i("kek", ">>>> ${this.displayName} estimate")
        val sum = elements.sumOf {
            val estimate = it.estimate()
//            Log.i("kek", "< ${it.displayName} ${estimate}")
            estimate?.totalSeconds ?: 0
        }
//        Log.i("kek", "<<<< ${this.displayName} estimated: $sum")
        return sum.toSeconds()
    }

    override fun getElement(name: ElementName): Element? {
        return if (this.name == name) {
            this
        } else {
            elements.firstNotNullOfOrNull { it.getElement(name) }
        }
    }

    override fun isLeaf(): Boolean {
        return false
    }

    override fun isLeafGroup(): Boolean {
        return elements.all {
            it.isLeaf()
        }
    }

    override fun nonEstimated(): List<Element> {
        return elements.map {
            it.nonEstimated()
        }.flatten()
    }

    override fun queues(): List<Queue> {
        return listOf(this) + elements.map {
            it.queues()
        }.flatten()
    }

    override fun tasks(): List<Task> {
        return elements.map {
            it.tasks()
        }.flatten()
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

    override fun withTaskClicked(clickedTask: Task): Element {
        return copy(elements = elements.map {
            it.withTaskClicked(clickedTask)
        })
    }

    override fun withTaskCompleted(completedTask: Task): Element {
        return copy(elements = elements.map {
            it.withTaskCompleted(completedTask)
        })
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return copy(elements = elements.map {
            it.withTaskLongClicked(clickedTask)
        })
    }

    override fun withTaskStatus(status: TaskStatus): Element {
        return copy(elements = elements.map {
            it.withTaskStatus(status)
        })
    }

    override fun withTaskToggled(toggledTask: Task): Element {
        return copy(elements = elements.map {
            it.withTaskToggled(toggledTask)
        })
    }

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }
}
