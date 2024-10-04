package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

@Serializable
sealed interface Element {
    val name: ElementName
    val displayName: String
    val active: Boolean
    fun estimate(): TimeValue?
    fun getElement(name: ElementName): Element?
    fun isLeaf(): Boolean
    fun isLeafGroup(): Boolean
    fun nonEstimated(): List<Element>
    fun queues(): List<Queue>
    fun tasks(): List<Task>
    fun withDoneReset(): Element
    fun withElement(name: ElementName, action: (element: Element) -> Element): Element
    fun withEstimate(name: ElementName, estimate: TimeValue?): Element
    fun withTaskStatus(status: TaskStatus): Element
    fun withTaskClicked(clickedTask: Task): Element
    fun withTaskCompleted(completedTask: Task): Element
    fun withTaskLongClicked(clickedTask: Task): Element
    fun withTaskToggled(toggledTask: Task): Element
    fun withActive(active: Boolean): Element
}
