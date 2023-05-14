@file:OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class, ExperimentalUnitApi::class)
@file:Suppress("USELESS_CAST")

package com.spqrta.state

import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import com.spqrta.state.app.App
import com.spqrta.state.app.action.AppReadyAction
import com.spqrta.state.app.action.FlipperAction
import com.spqrta.state.app.action.OnResumeAction
import com.spqrta.state.app.features.core.AppState
import com.spqrta.state.app.features.daily.personas.productive.Flipper
import com.spqrta.state.app.features.daily.personas.productive.Hour
import com.spqrta.state.app.features.daily.personas.productive.SectionPayload
import com.spqrta.state.app.state.*
import com.spqrta.state.app.state.optics.AppStateOptics
import com.spqrta.state.app.view_state.ButtonForm
import com.spqrta.state.app.view_state.FlipperView
import com.spqrta.state.app.view_state.StubView
import com.spqrta.state.app.view_state.TimeredPromptForm
import com.spqrta.state.app.view_state.ViewState
import com.spqrta.state.ui.Landscape
import com.spqrta.state.ui.Orientation
import com.spqrta.state.ui.Portrait
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
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val orientation = when (val rotation = windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> Portrait
            Surface.ROTATION_90 -> Landscape
            Surface.ROTATION_180 -> Portrait
            Surface.ROTATION_270 -> Portrait
            else -> throw IllegalArgumentException("Unknown rotation $rotation")
        }
        setContent {
            AppView(orientation)
        }
    }

    override fun onResume() {
        super.onResume()
        App.handleAction(OnResumeAction())
    }
}

@Composable
fun AppView(orientation: Orientation) {
    val state = App.state.collectAsState().value

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            when (orientation) {
                Portrait -> Portrait(state)
                Landscape -> Landscape(state)
            }
        }
    }
}

@Composable
fun Portrait(state: AppState) {
    Column(
        Modifier
            .padding(
                top = Dp(32f),
                bottom = Dp(16f),
                start = Dp(16f),
                end = Dp(16f),
            )
    ) {
        Box {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dp(8f))
            ) {
                Button(
                    onClick = {
                        App.handleAction(AppReadyAction.ResetDayAction)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Grey,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = Dp(8f)),
                ) {
                    Text(
                        text = "Reset day",
                        Modifier.align(Alignment.CenterVertically),
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Text(
            text = "State",
            Modifier.align(Alignment.CenterHorizontally),
            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold
        )
        MainView(state, Portrait)
    }
}

@Composable
fun Landscape(state: AppState) {
    MainView(state, Landscape)
}


@Composable
fun MainView(state: AppState, orientation: Orientation) {
    FrameView(AppStateOptics.optViewState.getStrict(state), orientation)
}

@Composable
fun FrameView(viewState: ViewState, orientation: Orientation) {
    when (orientation) {
        Portrait -> {
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
                    ControlsView(ViewState.optControls.getStrict(viewState), orientation)
                }
            }
        }

        Landscape -> {
            Box(Modifier.fillMaxSize()) {
                Row {
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(start = Dp(16f))
                            .fillMaxSize()
                    ) {
                        ViewStateView(viewState)
                    }
                    Box(Modifier.wrapContentWidth()) {
                        ControlsView(ViewState.optControls.getStrict(viewState), orientation)
                    }
                }
            }
        }
    }
}

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
    } as Any?
}

@Composable
fun HourSection(hour: Hour, section: SectionPayload?) {
    Box(
        Modifier.fillMaxWidth()
    ) {
        if (section == null) {
            Text("--")
        } else {
            Text(section.letter, Modifier.clickable {
                App.handleAction(FlipperAction.Delete(hour.id, section.number))
            })
        }
    }
}

@Composable
fun DayActivityStatItem(item: SectionPayload, flipper: Flipper) {
    Row(Modifier.fillMaxWidth()) {
        Box(Modifier.width(100f.dp)) {
            Text(item.name)
        }
        Row(Modifier.fillMaxWidth()) {
            val count = flipper.count(item)
            Text(
                count.toString(),
                Modifier
                    .padding(end = Dp(8f))
                    .width(32f.dp)
            )
            (0 until count).forEach {
                Text("*")
            }
        }
    }
}

@Composable
fun ControlsView(controls: List<Control>, orientation: Orientation) {
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
                            .align(Alignment.CenterHorizontally).let {
                                when (orientation) {
                                    Landscape -> it.padding(end = Dp(8f))
                                    Portrait -> it
                                }
                            },
                        colors = when (control.style) {
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
                        Box(
                            when (orientation) {
                                Portrait -> Modifier.fillMaxWidth()
                                Landscape -> Modifier
                                    .width(Dp(100f))
                            }
                        ) {
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

@Composable
fun Title(text: String) {
    Box(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .align(alignment = Alignment.Center),
            text = text,
            fontSize = TextUnit(32f, TextUnitType.Sp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun FlipperHour(hour: Hour) {
    Column {
        Text(text = hour.id.toString())
        HourSection(hour = hour, section = hour.section0)
        HourSection(hour = hour, section = hour.section1)
        HourSection(hour = hour, section = hour.section2)
        HourSection(hour = hour, section = hour.section3)
    }
}
