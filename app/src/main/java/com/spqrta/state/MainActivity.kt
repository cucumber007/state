@file:OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class, ExperimentalUnitApi::class)
@file:Suppress("USELESS_CAST")

package com.spqrta.state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.spqrta.state.app.App
import com.spqrta.state.app.action.OnResumeAction
import com.spqrta.state.app.features.core.AppState
import com.spqrta.state.app.state.*
import com.spqrta.state.app.state.optics.AppStateOptics
import com.spqrta.state.app.view_state.ButtonForm
import com.spqrta.state.app.view_state.StubView
import com.spqrta.state.app.view_state.TimeredPromptForm
import com.spqrta.state.app.view_state.ViewState
import com.spqrta.state.ui.TimerView
import com.spqrta.state.ui.control.Button
import com.spqrta.state.ui.control.Control
import com.spqrta.state.ui.control.Main
import com.spqrta.state.ui.control.Ordinal
import com.spqrta.state.ui.theme.AppTheme
import com.spqrta.state.ui.theme.Grey
import com.spqrta.state.ui.theme.Teal200

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppView()
        }
    }

    override fun onResume() {
        super.onResume()
        App.handleAction(OnResumeAction())
    }
}

@Composable
fun AppView() {
    val state = App.state.collectAsState().value

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                Modifier
                    .padding(
                        top = Dp(32f),
                        bottom = Dp(16f),
                        start = Dp(16f),
                        end = Dp(16f),
                    )
            ) {
                Text(
                    text = "State",
                    Modifier.align(Alignment.CenterHorizontally),
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )
                MainView(state)
            }
        }
    }
}

@Composable
fun MainView(state: AppState) {
    FrameView(AppStateOptics.optViewState.getStrict(state))
}

@Composable
fun FrameView(viewState: ViewState) {
    Box(Modifier.fillMaxSize()) {
        Column {
            Box(
                Modifier
                    .weight(1f)
                    .padding(top = Dp(8f))
                    .fillMaxSize()
            ) {
                ViewStateView(viewState)
            }
            ControlsView(ViewState.optControls.getStrict(viewState))
        }
    }
}

@Composable
fun ViewStateView(viewState: ViewState) {
    when (viewState) {
        is ButtonForm -> {
            Column {
                Box(Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.Center),
                        text = viewState.text,
                        fontSize = TextUnit(32f, TextUnitType.Sp),
                        textAlign = TextAlign.Center,
                    )
                }
                if(viewState.timer != null) {
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
    } as Any?
}

@Composable
fun ControlsView(controls: List<Control>) {
    Column {
        controls.forEach { control ->
            when (control) {
                is Button -> {
                    Button(
                        onClick = {
                            App.handleAction(control.action)
                        },
                        modifier = Modifier
                            .padding(
                                top = Dp(0f),
                                bottom = Dp(0f)
                            )
                            .align(Alignment.CenterHorizontally),
                        colors = when(control.style) {
                            Main -> {
                                ButtonDefaults.buttonColors(
                                    backgroundColor = Teal200,
                                    contentColor = Color.Black
                                )
                            }
                            Ordinal -> {
                                ButtonDefaults.buttonColors(
                                    backgroundColor = Grey,
                                    contentColor = Color.Black
                                )
                            }
                        }
                    ) {
                        Box(Modifier.fillMaxWidth()) {
                            Text(textAlign = TextAlign.Center, text = control.text)
                        }
                    }
                }
            } as Any?
        }
    }
}

@Composable
fun TimerView(view: TimerView) {
    Box(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            fontSize = TextUnit(24f, TextUnitType.Sp),
            text = view.stringValue,
            textAlign = TextAlign.Center,
        )
    }
}
