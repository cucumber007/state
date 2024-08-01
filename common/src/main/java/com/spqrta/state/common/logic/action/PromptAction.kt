package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.Prompt
import com.spqrta.state.common.logic.features.daily.routine.Routine
import com.spqrta.state.common.logic.features.daily.timers.TimerId

sealed interface PromptAction : AppAction {
    sealed class Action : PromptAction
    data class AddPrompt(val prompt: Prompt) : Action()
    data class TimeredPromptResolved(val timerId: TimerId) : Action()
    data class RoutinePromptResolved(val routine: Routine) : Action()
}
