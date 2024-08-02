package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

@Serializable
data class Gtd2State(
    val metaState: MetaState
) {
    companion object {
        val INITIAL = Gtd2State(
            MetaState(
                workdayStarted = false
            )
        )
    }
}