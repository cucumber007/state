package com.spqrta.state.app.features.daily.routine

import kotlinx.serialization.Serializable

@Serializable
sealed class Routine
@Serializable
object CleanTeeth : Routine()
