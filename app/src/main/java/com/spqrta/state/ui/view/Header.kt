@file:OptIn(ExperimentalUnitApi::class)

package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.spqrta.state.app.App
import com.spqrta.state.app.action.AppReadyAction
import com.spqrta.state.app.features.core.AppState
import com.spqrta.state.ui.theme.Grey

@Composable
fun Header(state: AppState) {
    Box {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = Dp(8f))
        ) {
            Button(
                onClick = {
                    App.handleAction(AppReadyAction.ResetDayAction)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Grey,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = Dp(8f)),
            ) {
                Text(
                    text = "Reset day",
                    Modifier.align(Alignment.CenterVertically),
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
