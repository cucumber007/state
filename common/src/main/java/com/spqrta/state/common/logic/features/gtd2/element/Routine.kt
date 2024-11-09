package com.spqrta.state.common.logic.features.gtd2.element

import android.annotation.SuppressLint
import android.content.Context
import com.spqrta.dynalist.utility.pure.nullIfEmpty
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineContext
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineTrigger
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@SuppressLint("NewApi")
@Serializable
data class Routine<Context : RoutineContext>(
    private val element: Element,
    override val displayName: String = "(R) ${element.name}",
    override val active: Boolean = true,
    val trigger: RoutineTrigger<Context>? = null,
    override val name: ElementName = ElementName.OtherName(element.name.value),
) : Element, ToBeDone {

    constructor(name: String) : this(
        Task(name)
    )

    val innerElement = element.withActive(if (!active) false else element.active)
    override val estimate: TimeValue =
        innerElement.toBeDone().nullIfEmpty()?.firstOrNull()?.estimate
            ?: 1.toSeconds()
    override val status: TaskStatus = innerElement.toBeDone().nullIfEmpty()?.firstOrNull()?.status
        ?: TaskStatus.Inactive

    override fun estimate(): TimeValue? {
        return innerElement.estimate()
    }

    override fun getElement(name: ElementName): Element? {
        return if (this.name == name) {
            this
        } else {
            innerElement.getElement(name)
        }
    }

    override fun getToBeDone(name: ElementName): ToBeDone? {
        return if (this.name == name) {
            this
        } else {
            innerElement.getToBeDone(name)
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

    override fun toBeDone(): List<ToBeDone> {
        return when (innerElement) {
            is Flipper -> innerElement.toBeDone()
            is Queue -> innerElement.toBeDone()
            is Task,
            is Routine<*> -> listOf(this)
        }
    }

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }

    override fun withDoneReset(): Element {
        return copy(
            element = element.withDoneReset(),
            active = true
        )
    }

    override fun withElement(name: ElementName, action: (element: Element) -> Element): Element {
        return if (name == this.name) {
            action(this)
        } else {
            copy(element = element.withElement(name, action))
        }
    }

    override fun withEstimate(name: ElementName, estimate: TimeValue?): Element {
        return copy(element = element.withEstimate(name, estimate))
    }

    override fun withStatus(status: TaskStatus): ToBeDone {
        return when (element) {
            is Task -> copy(element = element.withStatus(status))
            is Routine<*> -> copy(element = element.withStatus(status) as Element)
            else -> this
        }
    }

    fun <T : RoutineContext> castContext(castFun: (Context) -> T): Routine<T> {
        return Routine(
            element = element,
            displayName = displayName,
            active = active,
            trigger = trigger as RoutineTrigger<T>?,
            name = name
        )
    }

    fun withNewContext(context: MetaState): Routine<Context> {
        return if (trigger != null) {
            val (newTrigger, newActive) = trigger.updateContext(context, this)
            copy(trigger = newTrigger, active = newActive)
        } else this
    }
}
