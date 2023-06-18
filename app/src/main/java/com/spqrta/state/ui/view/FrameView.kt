package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.spqrta.state.app.view_state.ViewState
import com.spqrta.state.ui.Landscape
import com.spqrta.state.ui.Orientation
import com.spqrta.state.ui.Portrait

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
