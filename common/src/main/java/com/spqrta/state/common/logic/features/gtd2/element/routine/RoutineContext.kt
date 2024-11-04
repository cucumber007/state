package com.spqrta.state.common.logic.features.gtd2.element.routine

import com.spqrta.state.common.util.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed class RoutineContext {
    @Serializable
    object NoContext : RoutineContext()

    @Serializable
    data class Day(
        @Serializable(with = LocalDateSerializer::class)
        val day: LocalDate
    ) : RoutineContext()
}