package com.spqrta.state.ui.view.tinder

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderPrompt

@Composable
fun TinderPromptView(prompt: TinderPrompt) {
    when (prompt) {
        is TinderPrompt.NonEstimatedRoutine -> {
            Column {
                Text("Non-estimated Routine: ${prompt.routine.name}")
            }
        }

        is TinderPrompt.NonEstimatedTask -> {
            Text("Non-estimated Task: ${prompt.task.name}")
        }
    }
}