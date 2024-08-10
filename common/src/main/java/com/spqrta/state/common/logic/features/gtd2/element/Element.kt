package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

@Serializable
sealed interface Element {
    val name: String
    val displayName: String
    val active: Boolean
    fun estimate(): TimeValue?
    fun nonEstimated(): List<Element>
    fun withTaskClicked(clickedTask: Task): Element
    fun withTaskLongClicked(clickedTask: Task): Element
    fun withStatus(active: Boolean): Element
}

