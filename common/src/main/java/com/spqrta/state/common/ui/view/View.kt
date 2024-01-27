package com.spqrta.state.common.ui.view

import com.spqrta.state.common.util.Seconds
import com.spqrta.state.common.util.TimeValueFormatter

sealed class View
data class TimerUiView(val value: Seconds) : View() {
    val stringValue = TimeValueFormatter.formatTimeValue(value)
}
