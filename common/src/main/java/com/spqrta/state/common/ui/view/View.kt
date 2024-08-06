package com.spqrta.state.common.ui.view

import com.spqrta.state.common.util.time.Seconds
import com.spqrta.state.common.util.time.TimeValueFormatter

sealed class View
data class TimerUiView(val value: Seconds) : View() {
    val stringValue = TimeValueFormatter.formatTimeValue(value)
}
