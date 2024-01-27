@file:OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class, ExperimentalUnitApi::class)
@file:Suppress("USELESS_CAST")

package com.spqrta.state.ui

import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.spqrta.state.common.app.action.OnResumeAction
import com.spqrta.state.common.app.features.core.AppNotInitialized
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.app.features.core.AppState
import com.spqrta.state.common.app.state.optics.AppStateOptics
import com.spqrta.state.ui.theme.AppTheme
import com.spqrta.state.ui.view.FrameView
import com.spqrta.state.ui.view.Header


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
        com.spqrta.state.common.app.App.handleAction(OnResumeAction())
    }
}

@Composable
fun AppView(orientation: Orientation) {
    val state = com.spqrta.state.common.app.App.state.collectAsState().value
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
