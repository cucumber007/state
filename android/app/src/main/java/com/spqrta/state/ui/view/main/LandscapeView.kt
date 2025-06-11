package com.spqrta.state.ui.view.main

import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.ui.Landscape

// A wrapper around MainView to display it in landscape orientation
@Composable
fun LandscapeView(state: AppState) {
    MainView(state, Landscape)
}