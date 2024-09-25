package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    override val active: Boolean = true,
    override val name: ElementName.TaskName,
    val estimate: TimeValue? = null,
    val done: Boolean = false,
    override val displayName: String = name.value,
) : Element {

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

    val status: TaskStatus
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

    override fun getElement(name: ElementName): Element? {
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

    override fun nonEstimated(): List<Element> {
        return if (estimate == null && status == TaskStatus.Active) {
            listOf(this)
        } else {
            listOf()
        }
    }

    override fun queues(): List<Queue> {
        return listOf()
    }

    override fun tasks(): List<Task> {
        return listOf(this)
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

    fun withStatus(status: TaskStatus): Task {
        return when (status) {
            TaskStatus.Active -> copy(active = true, done = false)
            TaskStatus.Done -> copy(active = true, done = true)
            TaskStatus.Inactive -> copy(active = false, done = false)
        }
    }

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }

    override fun withTaskClicked(clickedTask: Task): Element {
        return if (this.name == clickedTask.name) {
            when (status) {
                TaskStatus.Active -> withStatus(TaskStatus.Done)
                TaskStatus.Done -> withStatus(TaskStatus.Done)
                TaskStatus.Inactive -> withStatus(TaskStatus.Inactive)
            }
        } else {
            this
        }
    }

    override fun withTaskCompleted(completedTask: Task): Element {
        return if (this.name == completedTask.name) {
            withStatus(TaskStatus.Done)
        } else {
            this
        }
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return if (this.name == clickedTask.name) {
            when (status) {
                TaskStatus.Active -> withStatus(TaskStatus.Inactive)
                TaskStatus.Done -> withStatus(TaskStatus.Active)
                TaskStatus.Inactive -> withStatus(TaskStatus.Active)
            }
        } else {
            this
        }
    }

    override fun withTaskStatus(status: TaskStatus): Element {
        return withStatus(status)
    }

    override fun withTaskToggled(toggledTask: Task): Element {
        return if (this.name == toggledTask.name) {
            when (status) {
                TaskStatus.Active -> withStatus(TaskStatus.Done)
                TaskStatus.Done -> withStatus(TaskStatus.Active)
                TaskStatus.Inactive -> withStatus(TaskStatus.Inactive)
            }
        } else {
            this
        }
    }

}
