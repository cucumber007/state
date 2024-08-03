package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
class Routine(
    override val name: String,
    val active: Boolean = true,
    val task: Task,
    val trigger: RoutineTrigger = RoutineTrigger.Day(LocalDate.now()),
) : Element