package com.spqrta.state.ui.view.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.AppGlobalAction
import com.spqrta.state.ui.Portrait
import com.spqrta.state.ui.view.Header
import com.spqrta.state.ui.view.common.controls.ImageActionButton

// A wrapper around MainView to display it in portrait orientation
@Composable
fun PortraitView(state: AppReady) {

    Column {
        Box(Modifier.align(Alignment.End)) {
            ImageActionButton(
                imageVector = Icons.Default.Build,
                action = AppGlobalAction.OnDebugMenuButtonClick,
                tint = Color.LightGray,
                size = 32.dp
            )
        }
        Column(
            Modifier
                .padding(
                    bottom = Dp(16f),
                    start = Dp(16f),
                    end = Dp(16f),
                )
        ) {
            Header(state)
            MainView(state, Portrait)
        }
    }


}

@Preview
@Composable
fun PortraitViewPreview() {
    PortraitView(AppReady.INITIAL.copy(showDebugMenu = false))
}
