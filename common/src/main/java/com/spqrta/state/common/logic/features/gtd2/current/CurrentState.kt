package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
data class CurrentState(
    val activeElement: ActiveElement?,
    val queuesToChoose: List<Queue>
) {

    companion object {
        val INITIAL = CurrentState(
            activeElement = null,
            // filled up on StateLoadedAction
            queuesToChoose = listOf()
        )

        val optActiveElement = ({ state: CurrentState ->
            state.activeElement
        } to { state: CurrentState, subState: ActiveElement ->
            state.copy(
                activeElement = subState
            )
        }).asOpticOptional()

        val optActiveQueue = ({ state: CurrentState ->
            if (state.activeElement is ActiveElement.ActiveQueue) {
                state.activeElement
            } else {
                null
            }
        } to { state: CurrentState, subState: ActiveElement.ActiveQueue ->
            state.copy(
                activeElement = subState
            )
        }).asOpticOptional()
    }
}
