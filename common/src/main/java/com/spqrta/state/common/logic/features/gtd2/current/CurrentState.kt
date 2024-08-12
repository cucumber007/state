package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
data class CurrentState(
    val activeElement: ActiveElement?
) {
    companion object {
        val INITIAL = CurrentState(null)

        val optActiveElement = ({ state: CurrentState ->
            state.activeElement
        } to { state: CurrentState, subState: ActiveElement ->
            state.copy(
                activeElement = subState
            )
        }).asOpticOptional()
    }
}

