package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.spqrta.state.app.App
import com.spqrta.state.ui.Landscape
import com.spqrta.state.ui.Orientation
import com.spqrta.state.ui.Portrait
import com.spqrta.state.ui.control.Button
import com.spqrta.state.ui.control.Control
import com.spqrta.state.ui.control.Main
import com.spqrta.state.ui.control.Ordinal
import com.spqrta.state.ui.theme.Grey
import com.spqrta.state.ui.theme.Teal200

@Composable
fun ControlsView(controls: List<Control>, orientation: Orientation) {
    Column {
        controls.forEach { control ->
            when (control) {
                is Button -> {
                    Button(
                        onClick = {
                            App.handleAction(control.action)
                        },
                        modifier = Modifier
                            .padding(
                                top = Dp(0f),
                                bottom = Dp(0f)
                            )
                            .align(Alignment.CenterHorizontally).let {
                                when (orientation) {
                                    Landscape -> it.padding(end = Dp(8f))
                                    Portrait -> it
                                }
                            },
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
                                Portrait -> Modifier.fillMaxWidth()
                                Landscape -> Modifier
                                    .width(Dp(100f))
                            }
                        ) {
                            Text(textAlign = TextAlign.Center, text = control.text)
                        }
                    }
                }
            } as Any?
        }
    }
}
