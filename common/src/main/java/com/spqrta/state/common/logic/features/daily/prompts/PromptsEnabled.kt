package com.spqrta.state.common.logic

import com.spqrta.state.common.logic.action.PromptAction
import com.spqrta.state.common.logic.action.TimerAction
import com.spqrta.state.common.logic.features.daily.timers.PromptTimer
import com.spqrta.state.common.logic.features.global.ActionEffect
import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.util.collections.asSet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.effectIf
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.serialization.Serializable

@Serializable
object PromptsEnabled {
    override fun toString(): String = javaClass.simpleName

    fun reduce(
        action: PromptAction,
        oldPrompts: List<Prompt>
    ): Reduced<List<Prompt>, out AppEffect> {
        return when (action) {
            is PromptAction.AddPrompt -> {
                val prompt = action.prompt
                (oldPrompts + prompt).withEffects(
                    if (prompt is TimeredPrompt) {
                        ActionEffect(TimerAction.StartTimer(prompt.timerId)).asSet()
                    } else {
                        setOf()
                    }
                )
            }

            is TimerAction.TimerEnded -> {
                oldPrompts.withEffects(
                    effectIf(action.timerId is PromptTimer) {
                        ActionEffect(PromptAction.TimeredPromptResolved(action.timerId))
                    }
                )
            }

            is PromptAction.TimeredPromptResolved -> {
                (oldPrompts.filter {
                    !(it is TimeredPrompt && it.timerId == action.timerId)
                }).withEffects()
            }

            is PromptAction.RoutinePromptResolved -> {
                (oldPrompts.filter {
                    !(it is RoutinePrompt && it.routine == action.routine)
                }).withEffects()
            }
        }
    }
}
