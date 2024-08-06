package com.spqrta.state.common.util.time

object TimeValueFormatter {
    fun formatTimeValue(timeValue: TimeValue): String {
        return PositiveSeconds(timeValue).let { positiveSeconds ->
            if (positiveSeconds.hours > 0) {
                listOf(
                    formatTwoDigit(positiveSeconds.hours),
                    formatTwoDigit(positiveSeconds.minutes),
                    formatTwoDigit(positiveSeconds.seconds)
                ).joinToString(":")
            } else {
                listOf(
                    formatTwoDigit(positiveSeconds.minutes),
                    formatTwoDigit(positiveSeconds.seconds)
                ).joinToString(":")
            }
        }
    }

    private fun formatTwoDigit(value: Int): String {
        if (value < 10) {
            return "0${value}"
        } else {
            return value.toString()
        }
    }
}
