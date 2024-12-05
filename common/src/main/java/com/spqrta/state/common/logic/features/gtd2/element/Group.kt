package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

sealed interface Group {
    val displayName: String
    val name: ElementName

    fun estimate(): TimeValue?
    fun format(): String
    fun isLeafGroup(): Boolean
    fun toBeDone(): List<ToBeDone>
    fun tasks(): List<Task>
}
