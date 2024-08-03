package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Routine(
    val active: Boolean = true,
    val task: Task,
    val trigger: RoutineTrigger = RoutineTrigger.Day(LocalDate.now()),
    override val name: String = task.name,
) : Element