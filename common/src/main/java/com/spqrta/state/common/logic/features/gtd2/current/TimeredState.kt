package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.util.serialization.LocalTimeSerializer
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
sealed class TimeredState {

    @Serializable
    data class Paused(
        val passed: TimeValue = TimeValue(0)
    ) : TimeredState()

    @Serializable
    data class Running(
        val passed: TimeValue,
        @Serializable(with = LocalTimeSerializer::class)
        val updatedAt: LocalTime,
    ) : TimeredState()

    val timePassed: TimeValue
        get() = when (this) {
            is Paused -> passed
            is Running -> passed
        }
}