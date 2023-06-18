package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.spqrta.state.app.features.daily.personas.productive.SectionPayload
import com.spqrta.state.app.view_state.ButtonForm
import com.spqrta.state.app.view_state.FlipperView
import com.spqrta.state.app.view_state.StubView
import com.spqrta.state.app.view_state.TimeredPromptForm
import com.spqrta.state.app.view_state.ViewState

@Composable
fun ViewStateView(viewState: ViewState) {
    when (viewState) {
        is ButtonForm -> {
            Column {
                Title(text = viewState.text)
                if (viewState.timer != null) {
                    Box(Modifier.fillMaxWidth()) {
                        TimerView(view = viewState.timer)
                    }
                }
            }
        }

        StubView -> {
            Text("")
        }

        is TimeredPromptForm -> {
            Column {
                Text(viewState.text)
                TimerView(view = viewState.timerView)
            }
        }

        is FlipperView -> {
            val flipper = viewState.flipper
            Column {
                Title(text = "Flipper")
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    flipper.hours.forEach {
                        Box(
                            Modifier
                                .padding(end = Dp(8f))
                                .weight(1f)
                        ) {
                            FlipperHour(hour = it)
                        }
                    }
                }
                Box(Modifier.padding(top = Dp(8f))) {
                    Column {
                        SectionPayload.all.forEach {
                            DayActivityStatItem(item = it, flipper = flipper)
                        }
                    }
                }
            }
        }
    } as Any?
}
