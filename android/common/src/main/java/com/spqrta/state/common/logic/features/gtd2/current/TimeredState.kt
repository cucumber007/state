package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.util.serialization.LocalTimeSerializer
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
sealed class TimeredState {

    @Serializable
    data class Paused(
        val passed: TimeValue,
        val notificationSent: Boolean
    ) : TimeredState() {
        companion object {
            val INITIAL = Paused(0.toSeconds(), false)
        }
    }

    @Serializable
    data class Running(
        val passed: TimeValue,
        @Serializable(with = LocalTimeSerializer::class)
        val updatedAt: LocalTime,
        val notificationSent: Boolean
    ) : TimeredState()

    val timePassed: TimeValue
        get() = when (this) {
            is Paused -> passed
            is Running -> passed
        }
}
