package com.spqrta.state.ui.view.common.controls

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.action.AppAction

@Composable
fun ActionButton(
    action: AppAction? = null,
    longPressAction: AppAction? = null,
    view: @Composable () -> Unit,
) {
    val actionState = rememberUpdatedState(action)
    val longPressActionState = rememberUpdatedState(longPressAction)
    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        actionState.value?.let {
                            App.handleAction(it)
                        }
                    },
                    onLongPress = {
                        longPressActionState.value?.let {
                            App.handleAction(it)
                        }
                    }
                )
            }
    ) {
        view()
    }
}