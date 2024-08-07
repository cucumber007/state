package com.spqrta.state.ui.view.common.controls

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.action.AppAction

@Composable
fun ImageActionButton(
    imageVector: ImageVector,
    action: AppAction,
    longPressAction: AppAction? = null,
    backgroundColor: Color? = null
) {
    Image(
        imageVector = imageVector,
        contentDescription = null,
        modifier = if (backgroundColor != null) {
            Modifier.background(backgroundColor)
        } else {
            Modifier
        }
            .padding(start = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        App.handleAction(action)
                    },
                    onLongPress = {
                        longPressAction?.let {
                            App.handleAction(it)
                        }
                    }
                )
            }
    )
}