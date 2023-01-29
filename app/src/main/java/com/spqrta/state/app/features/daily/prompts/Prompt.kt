package com.spqrta.state.app

import com.spqrta.state.app.action.AppAction
import com.spqrta.state.app.action.PromptAction
import com.spqrta.state.app.action.PromptAction.*
import com.spqrta.state.app.action.TimerAction
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.features.daily.timers.PromptTimer
import com.spqrta.state.app.features.daily.timers.TimerId
import com.spqrta.state.app.features.daily.timers.Timers
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.util.collections.asSet
import com.spqrta.state.util.optics.typeGet
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.effectIf
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.optics.wrap
import com.spqrta.state.util.state_machine.widen
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

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
object PromptsEnabled {
    override fun toString(): String = javaClass.simpleName

    fun reduce(action: PromptAction, oldPrompts: List<Prompt>): Reduced<List<Prompt>, out AppEffect> {
        return when (action) {
            is AddPrompt -> {
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
                        ActionEffect(PromptResolved(TimeredPrompt::class))
                    }
                )
            }
            is PromptResolved -> {
                (oldPrompts.filter { it::class != action.promptClass }).withEffects()
            }
        }
    }
}
