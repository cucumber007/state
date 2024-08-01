package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.daily.personas.productive.Hour

@Composable
fun FlipperHour(hour: Hour) {
    Column {
        Text(text = hour.id.toString())
        HourSection(hour = hour, section = hour.section0)
        HourSection(hour = hour, section = hour.section1)
        HourSection(hour = hour, section = hour.section2)
        HourSection(hour = hour, section = hour.section3)
    }
}
