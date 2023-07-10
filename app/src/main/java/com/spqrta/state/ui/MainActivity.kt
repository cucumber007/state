@file:OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class, ExperimentalUnitApi::class)
@file:Suppress("USELESS_CAST")

package com.spqrta.state.ui

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
import com.spqrta.state.app.features.core.AppNotInitialized
import com.spqrta.state.app.features.core.AppReady
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
import com.spqrta.state.ui.control.Button
import com.spqrta.state.ui.control.Control
import com.spqrta.state.ui.control.Main
import com.spqrta.state.ui.control.Ordinal
import com.spqrta.state.ui.theme.AppTheme
import com.spqrta.state.ui.theme.Grey
import com.spqrta.state.ui.theme.Teal200
import com.spqrta.state.ui.view.FrameView
import com.spqrta.state.ui.view.Header
import com.spqrta.state.ui.view.HourSection


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
    when (state) {
        AppNotInitialized -> {
            Text("App not initialized")
        }

        is AppReady -> {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (orientation) {
                        Portrait -> PortraitView(state)
                        Landscape -> LandscapeView(state)
                    }
                }
            }
        }
    }
}

@Composable
fun PortraitView(state: AppReady) {
    Column(
        Modifier
            .padding(
                top = Dp(32f),
                bottom = Dp(16f),
                start = Dp(16f),
                end = Dp(16f),
            )
    ) {
        Header(state)
//        Text(
//            text = "State",
//            Modifier.align(Alignment.CenterHorizontally),
//            fontSize = TextUnit(20f, TextUnitType.Sp),
//            fontWeight = FontWeight.Bold
//        )
        MainView(state, Portrait)
    }
}


@Composable
fun LandscapeView(state: AppState) {
    MainView(state, Landscape)
}


@Composable
fun MainView(state: AppState, orientation: Orientation) {
    FrameView(AppStateOptics.optViewState.getStrict(state), orientation)
}
