package com.spqrta.state.common.logic.features.daily.personas

import com.spqrta.state.common.logic.features.daily.timers.WorkTimer
import kotlinx.serialization.Serializable

@Serializable
sealed class ActivityState {
    override fun toString(): String = javaClass.simpleName
}

@Serializable
object None : ActivityState()

@Serializable
object Fiz : ActivityState()

@Serializable
data class Work(val timer: WorkTimer) : ActivityState()
