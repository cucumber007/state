package com.spqrta.state.app.action

import com.spqrta.state.app.Prompt
import kotlin.reflect.KClass

sealed interface PromptAction : AppAction {
    sealed class Action : PromptAction
    data class AddPrompt(val prompt: Prompt) : Action()
    data class PromptResolved(val promptClass: KClass<*>) : Action()
}
