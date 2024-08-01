package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.spqrta.state.ui.Landscape
import com.spqrta.state.ui.Orientation
import com.spqrta.state.ui.Portrait
import com.spqrta.state.common.ui.view.control.Button
import com.spqrta.state.common.ui.view.control.Control
import com.spqrta.state.common.ui.view.control.Main
import com.spqrta.state.common.ui.view.control.Ordinal
import com.spqrta.state.ui.theme.Grey
import com.spqrta.state.ui.theme.Teal200

@Composable
fun ControlsView(controls: List<Control>, orientation: Orientation) {
    val rows = if (controls.size % 2 == 0) {
        controls.size / 2
    } else {
        controls.size / 2 + 1
    }
    Column {
        for (i in 0 until rows) {
            Row(

            ) {
                ControlView(controls[i * 2], orientation)
                if (i * 2 + 1 < controls.size) {
                    ControlView(controls[i * 2 + 1], orientation)
                }
            }
        }
    }
}

@Composable
fun ControlView(control: Control, orientation: Orientation) {
    when (control) {
        is Button -> {
            Button(
                onClick = {
                    com.spqrta.state.common.logic.App.handleAction(control.action)
                },
                modifier = Modifier
                    .padding(
                        top = Dp(2f),
                        bottom = Dp(2f)
                    )
                    .height(Dp(40f)),
                colors = when (control.style) {
                    Main -> {
                        ButtonDefaults.buttonColors(
                            backgroundColor = Teal200,
                            contentColor = Color.Black
                        )
                    }

                    Ordinal -> {
                        ButtonDefaults.buttonColors(
                            backgroundColor = Grey,
                            contentColor = Color.Black
                        )
                    }
                }
            ) {
                Box(
                    when (orientation) {
//                        Portrait -> Modifier.fillMaxWidth()
                        Portrait -> Modifier.width(Dp(100f))
                        Landscape -> Modifier
                            .width(Dp(120f))
                    }
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = control.text,
                        fontSize = 12.sp
                    )
                }
            }
        }
    } as Any?
}
