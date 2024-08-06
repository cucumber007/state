package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable

@Serializable
sealed interface Element {
    val name: String
    val displayName: String
    val active: Boolean

    fun withTaskClicked(clickedTask: Task): Element
    fun withTaskLongClicked(clickedTask: Task): Element

    fun withStatus(active: Boolean): Element
}

