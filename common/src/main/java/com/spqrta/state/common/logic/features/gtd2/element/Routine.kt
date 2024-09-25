package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.RoutineTrigger
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Routine(
    private val element: Element,
    override val displayName: String = "${element.name} Routine",
    override val active: Boolean = true,
    val trigger: RoutineTrigger = RoutineTrigger.Day(LocalDate.now()),
    override val name: ElementName = ElementName.OtherName(displayName),
) : Element {

    constructor(name: String) : this(
        Task(name)
    )

    val innerElement = element.withActive(if (!active) false else element.active)

    override fun estimate(): TimeValue? {
        return innerElement.estimate()
    }

    override fun getElement(name: ElementName): Element? {
        return if (this.name == name) {
            this
        } else {
            element.getElement(name)
        }
    }

    override fun isLeaf(): Boolean {
        return innerElement.isLeaf()
    }

    override fun isLeafGroup(): Boolean {
        return innerElement.isLeafGroup()
    }

    override fun nonEstimated(): List<Element> {
        return if (innerElement.estimate() == null) {
            listOf(this)
        } else {
            listOf()
        }
    }

    override fun queues(): List<Queue> {
        return innerElement.queues()
    }

    override fun tasks(): List<Task> {
        return innerElement.tasks()
    }

    override fun withElement(name: ElementName, action: (element: Element) -> Element): Element {
        return if (name == this.name) {
            action(this)
        } else {
            innerElement.withElement(name, action)
        }
    }

    override fun withEstimate(name: ElementName, estimate: TimeValue?): Element {
        return copy(element = element.withEstimate(name, estimate))
    }

    override fun withTaskClicked(clickedTask: Task): Element {
        return if (innerElement.active) {
            this.copy(
                element = element.withTaskClicked(clickedTask)
            )
        } else {
            this
        }
    }

    override fun withTaskCompleted(completedTask: Task): Element {
        return if (innerElement.active) {
            this.copy(
                element = element.withTaskCompleted(completedTask)
            )
        } else {
            this
        }
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return if (innerElement.active) {
            this.copy(
                element = element.withTaskLongClicked(clickedTask)
            )
        } else {
            this
        }
    }

    override fun withTaskStatus(status: TaskStatus): Element {
        return innerElement.withTaskStatus(status)
    }

    override fun withTaskToggled(toggledTask: Task): Element {
        return if (innerElement.active) {
            this.copy(
                element = element.withTaskToggled(toggledTask)
            )
        } else {
            this
        }
    }

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }
}
