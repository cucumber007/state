package com.spqrta.state.common.logic.features.gtd2.element

import android.annotation.SuppressLint
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineContext
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineTrigger
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable
import java.time.LocalDate

@SuppressLint("NewApi")
@Serializable
data class Routine<Context : RoutineContext>(
    private val element: Element,
    override val displayName: String = "${element.name} Routine",
    override val active: Boolean = true,
    private val trigger: RoutineTrigger<Context>? = null,
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

    override fun mapRoutines(mapper: (Routine<*>) -> Routine<*>): Element {
        return mapper(this)
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

    override fun withDoneReset(): Element {
        return copy(element = element.withDoneReset())
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

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }

    fun withNewContext(context: MetaState): Routine<Context> {
        return if (trigger != null) {
            val (newTrigger, newActive) = trigger.updateContext(context)
            copy(trigger = newTrigger, active = newActive)
        } else this
    }
}
