package com.spqrta.state.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spqrta.state.app.App
import com.spqrta.state.app.action.ToDoListAction
import com.spqrta.state.app.features.daily.personas.productive.SectionPayload
import com.spqrta.state.app.view_state.ButtonForm
import com.spqrta.state.app.view_state.FlipperView
import com.spqrta.state.app.view_state.StubView
import com.spqrta.state.app.view_state.TimeredPromptForm
import com.spqrta.state.app.view_state.ToDoListView
import com.spqrta.state.app.view_state.ViewState

@Composable
fun ViewStateView(viewState: ViewState) {
    when (viewState) {
        is ButtonForm -> {
            Column {
                Title(text = viewState.text)
                if (viewState.timer != null) {
                    Box(Modifier.fillMaxWidth()) {
                        TimerView(view = viewState.timer)
                    }
                }
            }
        }

        StubView -> {
            Text("")
        }

        is TimeredPromptForm -> {
            Column {
                Text(viewState.text)
                TimerView(view = viewState.timerView)
            }
        }

        is FlipperView -> {
            val flipper = viewState.flipper
            Column {
                Title(text = "Flipper")
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    flipper.hours.forEach {
                        Box(
                            Modifier
                                .padding(end = Dp(8f))
                                .weight(1f)
                        ) {
                            FlipperHour(hour = it)
                        }
                    }
                }
                Box(Modifier.padding(top = Dp(8f))) {
                    Column {
                        SectionPayload.all.forEach {
                            DayActivityStatItem(item = it, flipper = flipper)
                        }
                    }
                }
            }
        }

        is ToDoListView -> {
            Column {
                Title(text = "To Do")
                Column(
                    Modifier.verticalScroll(rememberScrollState())
                ) {
                    viewState.toDoList.items.forEach { item ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = Dp(16f))
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            App.handleAction(
                                                ToDoListAction.OnPress(item.title)
                                            )
                                        }
                                    )
                                }
                        ) {
                            Row {
                                Column {
                                    Text(
                                        text = item.title,
                                        modifier = Modifier.padding(end = Dp(8f))
                                    )
                                    Text(
                                        text = item.reason ?: "",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .padding(end = Dp(8f))
                                    )
                                }
                                if (item.checked) {
                                    Image(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    } as Any?
}
