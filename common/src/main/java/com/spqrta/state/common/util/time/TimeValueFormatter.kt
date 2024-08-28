package com.spqrta.state.common.util.time

import java.time.LocalTime

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

    fun formatTimeValueWithoutSeconds(timeValue: TimeValue): String {
        return PositiveSeconds(timeValue).let { positiveSeconds ->
            if (positiveSeconds.justHours > 0) {
                listOf(
                    formatTwoDigit(positiveSeconds.justHours),
                    formatTwoDigit(positiveSeconds.justMinutes)
                ).joinToString(":")
            } else {
                listOf(
                    formatTwoDigit(positiveSeconds.justMinutes)
                ).joinToString(":")
            }
        }
    }

    fun formatLocalTimeWithoutSeconds(localTime: LocalTime): String {
        return listOf(
            formatTwoDigit(localTime.hour),
            formatTwoDigit(localTime.minute)
        ).joinToString(":")
    }

    private fun formatTwoDigit(value: Int): String {
        if (value < 10) {
            return "0${value}"
        } else {
            return value.toString()
        }
    }
}

fun LocalTime.formatWithoutSeconds(): String {
    return TimeValueFormatter.formatLocalTimeWithoutSeconds(this)
}
