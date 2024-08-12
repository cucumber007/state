package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

@Serializable
sealed interface Element {
    val name: String
    val displayName: String
    val active: Boolean
    fun getElement(name: String): Element?
    fun estimate(): TimeValue?
    fun nonEstimated(): List<Element>
    fun queues(): List<Queue>
    fun tasks(): List<Task>
    fun withElement(estimateName: String, action: (element: Element) -> Element): Element
    fun withEstimate(estimateName: String, estimate: TimeValue?): Element
    fun withTaskClicked(clickedTask: Task): Element
    fun withTaskCompleted(completedTask: Task): Element
    fun withTaskLongClicked(clickedTask: Task): Element
    fun withStatus(active: Boolean): Element
}

