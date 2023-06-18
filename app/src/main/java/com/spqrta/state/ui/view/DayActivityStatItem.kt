package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spqrta.state.app.features.daily.personas.productive.Flipper
import com.spqrta.state.app.features.daily.personas.productive.SectionPayload

@Composable
fun DayActivityStatItem(item: SectionPayload, flipper: Flipper) {
    Row(Modifier.fillMaxWidth()) {
        Box(Modifier.width(100f.dp)) {
            Text(item.name)
        }
        Row(Modifier.fillMaxWidth()) {
            val count = flipper.count(item)
            Text(
                count.toString(),
                Modifier
                    .padding(end = Dp(8f))
                    .width(32f.dp)
            )
            (0 until count).forEach {
                Text("*")
            }
        }
    }
}
