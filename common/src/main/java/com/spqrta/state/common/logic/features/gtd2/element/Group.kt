package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Group {
    val displayName: String
    val name: ElementName

    fun isLeafGroup(): Boolean
    fun toBeDone(): List<ToBeDone>
    fun tasks(): List<Task>
}
