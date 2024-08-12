package com.spqrta.state.common.logic.features.gtd2.current

import kotlinx.serialization.Serializable

@Serializable
data class CurrentState(
    val activeElement: ActiveElement? = null
) {
    companion object {
        val INITIAL = CurrentState()
    }
}

