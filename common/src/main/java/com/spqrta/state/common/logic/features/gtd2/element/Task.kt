package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toMinutes
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    override val active: Boolean = true,
    override val name: String,
    val estimate: TimeValue? = null,
    val done: Boolean = false,
    override val displayName: String = name,
) : Element {

    constructor(name: String, taskStatus: TaskStatus = TaskStatus.Active) : this(
        active = taskStatus != TaskStatus.Inactive,
        name = name,
        done = taskStatus == TaskStatus.Done
    )

    constructor(name: String, minutes: Int) : this(
        name = name,
        estimate = minutes.toMinutes()
    )

    val status: TaskStatus
        get() {
            return when {
                !active -> TaskStatus.Inactive
                done -> TaskStatus.Done
                else -> TaskStatus.Active
            }
        }

    override fun getElement(name: String): Element? {
        return if (this.name == name) {
            this
        } else {
            null
        }
    }

    override fun estimate(): TimeValue? {
        return estimate
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

    override fun withElement(estimateName: String, action: (element: Element) -> Element): Element {
        return if (estimateName == this.name) {
            action(this)
        } else {
            this
        }
    }

    override fun withEstimate(estimateName: String, estimate: TimeValue?): Element {
        return if (estimateName == this.name) {
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

    override fun withStatus(active: Boolean): Element {
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