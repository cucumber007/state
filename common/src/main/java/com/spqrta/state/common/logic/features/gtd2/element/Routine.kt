package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.RoutineTrigger
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Routine(
    private val element: Element,
    override val displayName: String = "${element.name} Routine",
    override val active: Boolean = true,
    val trigger: RoutineTrigger = RoutineTrigger.Day(LocalDate.now()),
    override val name: String = element.name,
) : Element {

    constructor(name: String) : this(
        Task(name)
    )

    val innerElement = element.withStatus(if (!active) false else element.active)

    override fun nonEstimated(): List<Element> {
        return if (innerElement.estimate() == null) {
            listOf(this)
        } else {
            listOf()
        }
    }

    override fun estimate(): TimeValue? {
        return innerElement.estimate()
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

    override fun withElement(name: String, action: (element: Element) -> Element): Element {
        return if (name == this.name) {
            action(this)
        } else {
            innerElement.withElement(name, action)
        }
    }

    override fun withEstimate(name: String, estimate: TimeValue?): Element {
        return copy(element = element.withEstimate(name, estimate))
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

    override fun withStatus(active: Boolean): Element {
        return copy(active = active)
    }
}