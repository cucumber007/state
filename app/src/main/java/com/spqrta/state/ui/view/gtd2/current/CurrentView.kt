package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.spqrta.state.Effects
import com.spqrta.state.common.environments.DateTimeEnvironment
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.effect.ViewEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.time.formatWithoutSeconds
import com.spqrta.state.common.util.time.toSeconds
import com.spqrta.state.ui.theme.FontSize
import com.spqrta.state.ui.view.common.controls.ActionButton
import com.spqrta.state.ui.view.common.controls.ImageActionButton
import java.time.LocalTime

@Composable
fun CurrentView(state: Gtd2State) {
    when (val activeElement: ActiveElement? = state.currentState.activeElement) {
        is ActiveElement.ActiveQueue -> {
            val estimate = activeElement.groupValue(state.tasksState).estimate() ?: 0.toSeconds()
            val finishTime = DateTimeEnvironment.timeNow
                .plusSeconds(estimate.totalSeconds)
                .formatWithoutSeconds()
            Column {
                Text(
                    text = "${DateTimeEnvironment.timeNow.formatWithoutSeconds()} | Finish at: $finishTime",
                    fontSize = FontSize.BASE,
                    modifier = Modifier.padding(start = 8.dp)
                )
                val activeTask = activeElement.activeTask.toNullable()
                if (activeTask == null) {
                    Column {
                        Text(
                            "Choose active task:",
                            fontSize = FontSize.TITLE,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        activeElement.activeTasksValue(state.tasksState).forEach {
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
                    Column {
                        Row {
                            ActionButton(
                                longPressAction = CurrentViewAction.OnResetActiveElementClick,
                            ) {
                                Text(
                                    activeElement.groupValue(state.tasksState).displayName,
                                    fontSize = FontSize.TITLE,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                ImageActionButton(
                                    imageVector = if (state.currentState.showDone) {
                                        Icons.Default.List
                                    } else {
                                        Icons.Default.ThumbUp
                                    },
                                    action = CurrentViewAction.OnToggleShowDoneClick
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                ImageActionButton(
                                    imageVector = if (state.currentState.showInactive) {
                                        Icons.Default.Build
                                    } else {
                                        Icons.Default.Check
                                    },
                                    action = CurrentViewAction.OnToggleShowInactiveClick
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                ImageActionButton(
                                    imageVector = Icons.Default.Home,
                                    action = CurrentViewAction.OnScrollToActiveClick
                                )
                            }
                        }
                        var itemPosition by remember { mutableStateOf(0F) }
                        val scrollState = rememberScrollState()
                        Column(
                            Modifier
                                .verticalScroll(scrollState)
                                .height(IntrinsicSize.Max)
                        ) {
                            LaunchedEffect(key1 = Unit) {
                                Effects.effects.collect {
                                    val mut = it.toMutableList()
                                    while (mut.isNotEmpty()) {
                                        val effect = mut.removeAt(0)
                                        when (effect) {
                                            is ViewEffect.Scroll -> {
                                                scrollState.scrollTo(itemPosition.toInt())

                                            }
                                        }
                                    }
                                    Effects.effects.emit(emptyList())
                                }
                            }

                            state.currentState.tasksToShowValue(state.tasksState).forEach {
                                if (it.name == activeTask.task.name) {
                                    ActionButton(
                                        longPressAction = CurrentViewAction.OnSubElementLongClick(it)
                                    ) {
                                        Box(modifier = Modifier.then(
                                            Modifier.onGloballyPositioned { layoutCoordinates ->
                                                itemPosition = layoutCoordinates.positionInRoot().y
                                            }
                                        ))
                                        ActiveTaskView(activeTask)
                                    }
                                } else {
                                    val (color, style) = when (it.status) {
                                        TaskStatus.Active -> Color.Black to TextStyle()
                                        TaskStatus.Done -> Color.Gray to TextStyle(textDecoration = TextDecoration.LineThrough)
                                        TaskStatus.Inactive -> Color.Gray to TextStyle()
                                    }
                                    ActionButton(
                                        action = CurrentViewAction.OnSubElementClick(it),
                                        longPressAction = CurrentViewAction.OnSubElementLongClick(it)
                                    ) {
                                        Text(
                                            text = it.displayName,
                                            fontSize = FontSize.BASE,
                                            modifier = Modifier.padding(
                                                start = 16.dp,
                                                bottom = 8.dp
                                            ),
                                            color = color,
                                            style = style
                                        )
                                    }
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
                state.currentState.groupsToChooseValue(state.tasksState).forEach {
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
