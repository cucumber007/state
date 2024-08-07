package com.spqrta.state.common.logic.features.gtd2.stats

import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

@Serializable
data class Gtd2Stats(
    val timeLeft: TimeValue
) {
    companion object {
        val INITIAL = Gtd2Stats(
            timeLeft = TimeValue(0)
        )
    }
}