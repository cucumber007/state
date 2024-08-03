package com.spqrta.state.common.logic

import com.spqrta.state.common.logic.features.daily.routine.RoutineLegacy
import com.spqrta.state.common.logic.features.daily.timers.TimerId
import com.spqrta.state.common.logic.optics.AppReadyOptics
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
data class RoutinePrompt(val routine: RoutineLegacy) : Prompt()
