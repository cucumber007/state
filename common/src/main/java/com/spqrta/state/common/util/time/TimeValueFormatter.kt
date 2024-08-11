package com.spqrta.state.common.util.time

object TimeValueFormatter {
    fun formatTimeValue(timeValue: TimeValue): String {
        return PositiveSeconds(timeValue).let { positiveSeconds ->
            if (positiveSeconds.justHours > 0) {
                listOf(
                    formatTwoDigit(positiveSeconds.justHours),
                    formatTwoDigit(positiveSeconds.justMinutes),
                    formatTwoDigit(positiveSeconds.justSeconds)
                ).joinToString(":")
            } else {
                listOf(
                    formatTwoDigit(positiveSeconds.justMinutes),
                    formatTwoDigit(positiveSeconds.justSeconds)
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
