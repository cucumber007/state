package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
sealed class ActiveElement {
    @Serializable
    data class ActiveQueue(
        val queue: Queue,
        val activeTask: TimeredTask? = null
    ) : ActiveElement()

    companion object {
        val optActiveTask = ({ state: ActiveElement ->
            when (state) {
                is ActiveQueue -> state.activeTask
            }
        } to { state: ActiveElement, subState: TimeredTask? ->
            when (state) {
                is ActiveQueue -> state.copy(
                    activeTask = subState
                )
            } as ActiveElement
        }).asOpticOptional()
    }
}

