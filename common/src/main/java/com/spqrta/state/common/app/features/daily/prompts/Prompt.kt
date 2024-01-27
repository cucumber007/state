package com.spqrta.state.common.app

import com.spqrta.state.common.app.features.daily.routine.Routine
import com.spqrta.state.common.app.features.daily.timers.TimerId
import com.spqrta.state.common.app.state.optics.AppReadyOptics
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.widen
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
sealed class Prompt(val priority: Int = 0) {
    companion object {
        val reducer = widen(
            typeGet(),
            AppReadyOptics.optPrompts,
            PromptsEnabled::reduce
        )
    }
}

@Suppress("SpellCheckingInspection")
@kotlinx.serialization.Serializable
data class TimeredPrompt(val timerId: TimerId) : Prompt()

@Serializable
data class RoutinePrompt(val routine: Routine) : Prompt()
