package com.spqrta.state.ui.view.gtd2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.action.CurrentAction
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.ui.theme.FontSize
import com.spqrta.state.ui.view.common.controls.ActionButton

@Composable
fun CurrentView(state: Gtd2State) {
    when (val activeElement: ActiveElement? = state.currentState.activeElement) {
        is ActiveElement.ActiveQueue -> {
            if (activeElement.activeTask == null) {
                Column {
                    Text("Choose active task:")
                }
            } else {
                Column {

                }
            }
        }

        null -> {
            Column {
                Text(
                    text = "No active element, choose one:",
                    fontSize = FontSize.TITLE,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                state.taskTree.queues().forEach {
                    ActionButton(action = CurrentAction.OnElementClicked(it)) {
                        Text(
                            text = it.displayName,
                            fontSize = FontSize.BASE,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}