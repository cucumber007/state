package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    override val name: String,
    val status: TaskStatus = TaskStatus.Active,
) : Element {
    override fun withTaskClicked(clickedTask: Task): Element {
        return if (this == clickedTask) {
            copy(
                status = when (status) {
                    TaskStatus.Active -> TaskStatus.Done
                    TaskStatus.Done -> TaskStatus.Done
                    TaskStatus.Inactive -> TaskStatus.Inactive
                }
            )
        } else {
            this
        }
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return if (this == clickedTask) {
            copy(
                status = when (status) {
                    TaskStatus.Active -> TaskStatus.Inactive
                    TaskStatus.Done -> TaskStatus.Active
                    TaskStatus.Inactive -> TaskStatus.Active
                }
            )
        } else {
            this
        }
    }
}