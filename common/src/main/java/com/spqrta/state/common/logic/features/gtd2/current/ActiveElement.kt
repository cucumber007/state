package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import kotlinx.serialization.Serializable

@Serializable
sealed class ActiveElement {
    @Serializable
    data class ActiveQueue(
        val queue: Queue,
        val activeTask: Task? = null
    ) : ActiveElement()
}