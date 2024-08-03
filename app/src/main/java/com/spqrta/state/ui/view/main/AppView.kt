package com.spqrta.state.ui.view.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.ui.Landscape
import com.spqrta.state.ui.Orientation
import com.spqrta.state.ui.Portrait
import com.spqrta.state.ui.theme.AppTheme

// The root view, defines orientation and displays MainView
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