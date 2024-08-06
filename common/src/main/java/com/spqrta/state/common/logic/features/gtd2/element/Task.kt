package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    override val active: Boolean = true,
    override val name: String,
    val done: Boolean = false,
    override val displayName: String = name,
) : Element {

    constructor(name: String, taskStatus: TaskStatus = TaskStatus.Active) : this(
        taskStatus != TaskStatus.Inactive,
        name,
        taskStatus == TaskStatus.Done
    )

    val status: TaskStatus
        get() {
            return when {
                !active -> TaskStatus.Inactive
                done -> TaskStatus.Done
                else -> TaskStatus.Active
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
        return if (this == clickedTask) {
            when (status) {
                TaskStatus.Active -> withStatus(TaskStatus.Done)
                TaskStatus.Done -> withStatus(TaskStatus.Done)
                TaskStatus.Inactive -> withStatus(TaskStatus.Inactive)
            }
        } else {
            this
        }
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return if (this == clickedTask) {
            when (status) {
                TaskStatus.Active -> withStatus(TaskStatus.Inactive)
                TaskStatus.Done -> withStatus(TaskStatus.Active)
                TaskStatus.Inactive -> withStatus(TaskStatus.Active)
            }
        } else {
            this
        }
    }
}