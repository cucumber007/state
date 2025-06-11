package com.spqrta.state.ui.view.common.controls

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.action.AppAction

@Composable
fun ImageActionButton(
    imageVector: ImageVector,
    action: AppAction,
    longPressAction: AppAction? = null,
    backgroundColor: Color? = null,
    tint: Color = Color.Black,
    size: Dp = Dp(24f)
) {
    val actionState = rememberUpdatedState(action)
    val longPressActionState = rememberUpdatedState(longPressAction)
    Box(
        Modifier
            .height(size)
            .width(size)
    ) {
        Image(
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(tint),
            contentDescription = null,
            modifier = if (backgroundColor != null) {
                Modifier.background(backgroundColor)
            } else {
                Modifier
            }
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            App.handleAction(actionState.value)
                        },
                        onLongPress = {
                            longPressActionState.value?.let {
                                App.handleAction(it)
                            }
                        }
                    )
                }
        )
    }
}
