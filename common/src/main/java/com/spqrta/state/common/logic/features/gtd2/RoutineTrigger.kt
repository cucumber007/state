package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed class RoutineTrigger {
    data class Day(val scope: LocalDate) : RoutineTrigger()
}