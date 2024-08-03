package com.spqrta.state.common.logic.features.daily.routine

import kotlinx.serialization.Serializable

@Serializable
sealed class RoutineLegacy {
    override fun toString(): String = javaClass.simpleName
}

@Serializable
object CleanTeeth : RoutineLegacy()
