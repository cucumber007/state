package com.spqrta.state.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.spqrta.state.common.logic.action.FlipperAction
import com.spqrta.state.common.logic.features.daily.personas.productive.Hour
import com.spqrta.state.common.logic.features.daily.personas.productive.SectionPayload

@Composable
fun HourSection(hour: Hour, section: SectionPayload?) {
    Box(
        Modifier.fillMaxWidth()
    ) {
        if (section == null) {
            Text("--")
        } else {
            Text(section.letter, Modifier.clickable {
                com.spqrta.state.common.logic.App.handleAction(
                    FlipperAction.Delete(
                        hour.id,
                        section.number
                    )
                )
            })
        }
    }
}
