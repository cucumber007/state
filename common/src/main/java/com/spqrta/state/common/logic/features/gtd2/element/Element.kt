package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable

@Serializable
sealed interface Element {
    val name: String

    fun withTaskClicked(task: Task): Element
    fun withTaskLongClicked(task: Task): Element
}

