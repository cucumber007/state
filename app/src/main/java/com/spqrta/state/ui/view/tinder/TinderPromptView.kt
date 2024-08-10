package com.spqrta.state.ui.view.tinder

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.action.TinderAction
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderPrompt
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.ui.view.common.controls.TextActionButton

@Composable
fun TinderPromptView(prompt: TinderPrompt) {
    when (prompt) {
        is TinderPrompt.NonEstimatedRoutine -> {
            Column {
                Text("Non-estimated Routine: ${prompt.routine.name}")
                PromptButtons(prompt, prompt.routine)
            }
        }

        is TinderPrompt.NonEstimatedTask -> {
            Column {
                Text("Non-estimated Task: ${prompt.task.name}")
                PromptButtons(prompt, prompt.task)
            }
        }
    }
}

@Composable
fun PromptButtons(prompt: TinderPrompt, element: Element) {
    TextActionButton(
        text = "15 min",
        action = TinderAction.OnEstimated(element, 15.toMinutes()),
    )
    TextActionButton(
        text = "Skip",
        action = TinderAction.OnSkipped(prompt),
    )
}