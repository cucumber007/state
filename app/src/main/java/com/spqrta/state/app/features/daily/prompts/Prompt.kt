package com.spqrta.state.app

import com.spqrta.state.app.action.PromptAction.*
import com.spqrta.state.app.features.daily.routine.Routine
import com.spqrta.state.app.features.daily.timers.TimerId
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.util.optics.typeGet
import com.spqrta.state.util.state_machine.widen
import kotlinx.serialization.Serializable

@Serializable
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
@Serializable
data class TimeredPrompt(val timerId: TimerId) : Prompt()

@Serializable
data class RoutinePrompt(val routine: Routine) : Prompt()
