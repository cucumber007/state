package com.spqrta.state.ui.view.persona

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.ProductiveNavigationAction
import com.spqrta.state.common.logic.features.daily.DailyState
import com.spqrta.state.common.logic.features.daily.personas.Productive
import com.spqrta.state.common.logic.features.daily.personas.productive.FlipperScreen

@Composable
fun PersonaHeader(dailyState: DailyState, resetStateEnabled: Boolean) {
    val persona = dailyState.persona
    if (persona is Productive) {
        Image(
            imageVector = if (persona.navigation is FlipperScreen) {
                Icons.Outlined.CheckCircle
            } else {
                Icons.Default.ArrowBack
            },
            contentDescription = null,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            App.handleAction(
                                ProductiveNavigationAction.OpenTodoListClicked
                            )
                        }
                    )
                }
        )
    }
    Image(
        imageVector = Icons.Default.Refresh,
        contentDescription = null,
        modifier = if (resetStateEnabled) {
            Modifier.background(Color.Red)
        } else {
            Modifier
        }
            .padding(start = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        App.handleAction(DebugAction.ResetState)
                    },
                    onLongPress = {
                        App.handleAction(
                            DebugAction.FlipResetStateEnabled
                        )
                    }
                )
            }
    )
}
