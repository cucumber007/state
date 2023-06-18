@file:OptIn(ExperimentalUnitApi::class)

package com.spqrta.state.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.spqrta.state.app.App
import com.spqrta.state.app.action.AppReadyAction
import com.spqrta.state.app.features.core.AppReady

@Composable
fun Header(state: AppReady) {
    Box {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = Dp(8f))
        ) {
            Image(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = if (state.resetStateEnabled) {
                    Modifier.background(Color.Red)
                } else {
                    Modifier
                }.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            App.handleAction(AppReadyAction.ResetDayAction)
                        },
                        onLongPress = {
                            App.handleAction(
                                AppReadyAction.FlipResetStateEnabledAction
                            )
                        }
                    )
                }
            )
        }
    }
}
