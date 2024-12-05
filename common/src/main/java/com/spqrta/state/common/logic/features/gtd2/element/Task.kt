package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    override val active: Boolean = true,
    override val name: ElementName.TaskName,
    override val estimate: TimeValue? = null,
    val done: Boolean = false,
    override val displayName: String = name.value,
) : Element, ToBeDone {

    constructor(name: String, taskStatus: TaskStatus = TaskStatus.Active) : this(
        active = taskStatus != TaskStatus.Inactive,
        name = ElementName.TaskName(name),
        done = taskStatus == TaskStatus.Done
    )

    constructor(name: String, minutes: Int) : this(
        name = ElementName.TaskName(name),
        estimate = minutes.toMinutes()
    )

    constructor(name: String, estimate: TimeValue?) : this(
        name = ElementName.TaskName(name),
        estimate = estimate
    )

    override val status: TaskStatus
        get() {
            return when {
                !active -> TaskStatus.Inactive
                done -> TaskStatus.Done
                else -> TaskStatus.Active
            }
        }

    override fun estimate(): TimeValue? {
        return when (status) {
            TaskStatus.Active -> estimate
            TaskStatus.Done,
            TaskStatus.Inactive -> 0.toSeconds()
        }
    }

    override fun format(): String {
        return name.value
    }

    override fun getElement(name: ElementName): Element? {
        return if (this.name == name) {
            this
        } else {
            null
        }
    }

    override fun getToBeDone(name: ElementName): ToBeDone? {
        return if (this.name == name) {
            this
        } else {
            null
        }
    }

    override fun isLeaf(): Boolean {
        return true
    }

    override fun isLeafGroup(): Boolean {
        return false
    }

    override fun map(mapper: (Element) -> Element): Element {
        return mapper(this)
    }

    override fun groups(): List<Group> {
        return listOf()
    }

    override fun nonEstimated(): List<Element> {
        return if (estimate == null && status == TaskStatus.Active) {
            listOf(this)
        } else {
            listOf()
        }
    }

    override fun tasks(): List<Task> {
        return listOf(this)
    }

    override fun toBeDone(): List<ToBeDone> {
        return listOf(this)
    }

    override fun withDoneReset(): Element {
        return copy(done = false)
    }

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }

    override fun withElement(name: ElementName, action: (element: Element) -> Element): Element {
        return if (name == this.name) {
            action(this)
        } else {
            this
        }
    }

    override fun withEstimate(name: ElementName, estimate: TimeValue?): Element {
        return if (name == this.name) {
            copy(estimate = estimate)
        } else {
            this
        }
    }

    override fun withNewContext(metaState: MetaState): Element {
        return this
    }

    override fun withStatus(status: TaskStatus): Task {
        return when (status) {
            TaskStatus.Active -> copy(active = true, done = false)
            TaskStatus.Done -> copy(active = true, done = true)
            TaskStatus.Inactive -> copy(active = false, done = false)
        }
    }

}
