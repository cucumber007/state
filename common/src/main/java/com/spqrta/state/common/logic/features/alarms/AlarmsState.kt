package com.spqrta.state.common.logic.features.alarms

import com.spqrta.state.common.util.serialization.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class AlarmsState(
    val alarms: List<Alarm> = emptyList(),
    @Serializable(with = LocalTimeSerializer::class)
    val currentTime: LocalTime = LocalTime.now()
)