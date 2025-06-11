package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

@Serializable
sealed interface ToBeDone {
    val active: Boolean
    val name: ElementName
    val displayName: String
    val estimate: TimeValue?
    val status: TaskStatus

    fun withStatus(status: TaskStatus): ToBeDone
}
