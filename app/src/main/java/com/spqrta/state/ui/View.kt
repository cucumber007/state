package com.spqrta.state.ui

import com.spqrta.state.util.Seconds
import com.spqrta.state.util.TimeValueFormatter

sealed class View
data class TimerView(val value: Seconds): View() {
    val stringValue = TimeValueFormatter.formatTimeValue(value)
}
