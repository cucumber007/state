package com.spqrta.state.app.features.daily.routine

import kotlinx.serialization.Serializable

@Serializable
sealed class Routine {
    override fun toString(): String = javaClass.simpleName
}

@Serializable
object CleanTeeth : Routine()
