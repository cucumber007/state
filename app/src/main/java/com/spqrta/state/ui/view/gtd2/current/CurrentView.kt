package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.ui.theme.FontSize
import com.spqrta.state.ui.view.common.controls.ActionButton

@Composable
fun CurrentView(state: Gtd2State) {
    when (val activeElement: ActiveElement? = state.currentState.activeElement) {
        is ActiveElement.ActiveQueue -> {
            if (activeElement.activeTask == null) {
                Column {
                    Text(
                        "Choose active task:",
                        fontSize = FontSize.TITLE,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    activeElement.activeTasks.forEach {
                        ActionButton(action = CurrentViewAction.OnSubElementClick(it)) {
                            Text(
                                text = it.displayName,
                                fontSize = FontSize.BASE,
                                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                            )
                        }
                    }
                }
            } else {
                val activeTask = activeElement.activeTask!!
                Column {
                    Text(
                        activeElement.queue.displayName,
                        fontSize = FontSize.TITLE,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState())
                            .height(IntrinsicSize.Max)
                    ) {
                        activeElement.tasksToShow.forEach {
                            if (it.name == activeTask.task.name) {
                                ActionButton(
                                    longPressAction = CurrentViewAction.OnSubElementLongClick(it)
                                ) {
                                    ActiveTaskView(activeTask)
                                }
                            } else {
                                ActionButton(
                                    action = CurrentViewAction.OnSubElementClick(it),
                                    longPressAction = CurrentViewAction.OnSubElementLongClick(it)
                                ) {
                                    Text(
                                        text = it.displayName,
                                        fontSize = FontSize.BASE,
                                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                                        color = if (it.status is TaskStatus.Done) {
                                            Color.Gray
                                        } else {
                                            Color.Black
                                        },
                                        style = if (it.status is TaskStatus.Done) {
                                            TextStyle(textDecoration = TextDecoration.LineThrough)
                                        } else {
                                            TextStyle()
                                        }
                                    )
                                }
                            }
                        }
                    }
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
                state.currentState.queuesToChoose.forEach {
                    ActionButton(action = CurrentViewAction.OnElementClick(it)) {
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
