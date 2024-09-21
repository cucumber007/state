package com.spqrta.state.ui.view.gtd2.tinder

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.action.TinderAction
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.meta.MetaProperty
import com.spqrta.state.common.logic.features.gtd2.meta.Type
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderPrompt
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.ui.view.common.controls.TextActionButton

@Composable
fun TinderPromptView(prompt: TinderPrompt) {
    when (prompt) {
        is TinderPrompt.NonEstimatedRoutine -> {
            Column {
                Text("Non-estimated Routine: ${prompt.routine.name}")
                EstimatePromptButtons(prompt, prompt.routine)
            }
        }

        is TinderPrompt.NonEstimatedTask -> {
            Column {
                Text("Non-estimated Task: ${prompt.task.name}")
                EstimatePromptButtons(prompt, prompt.task)
            }
        }

        is TinderPrompt.UnknownMetaState -> {
            Column {
                Text("Unknown meta state: ${prompt.property.name}")
                MetaPropertyPromptButtons(prompt, prompt.property)
            }
        }
    }
}

@Composable
fun EstimatePromptButtons(prompt: TinderPrompt, element: Element) {
    TextActionButton(
        text = "15 min",
        action = TinderAction.OnEstimated(element, 15.toMinutes()),
    )
    TextActionButton(
        text = "Skip",
        action = TinderAction.OnSkipped(prompt),
    )
}

@Composable
fun MetaPropertyPromptButtons(prompt: TinderPrompt, property: MetaProperty) {
    when (property.type) {
        Type.Boolean -> {
            TextActionButton(
                text = "Yes",
                action = TinderAction.OnMetaState(property, true),
            )
            TextActionButton(
                text = "No",
                action = TinderAction.OnMetaState(property, false),
            )
        }
    }
}
