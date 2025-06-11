package com.spqrta.state.common.logic.features.alarms

import com.spqrta.state.common.util.serialization.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class Alarm(
    val id: Int,
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime
)