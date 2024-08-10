package com.spqrta.state.ui.view.common.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.action.AppAction

@Composable
fun TextActionButton(
    text: String,
    action: AppAction,
    longPressAction: AppAction? = null,
    backgroundColor: Color? = null
) {
    val actionState = rememberUpdatedState(action)
    val longPressActionState = rememberUpdatedState(longPressAction)
    Text(
        text = text,
        modifier = if (backgroundColor != null) {
            Modifier.background(backgroundColor)
        } else {
            Modifier
        }
            .padding(start = 16.dp)
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