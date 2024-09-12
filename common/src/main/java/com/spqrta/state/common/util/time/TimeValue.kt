package com.spqrta.state.common.util.time

import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
open class TimeValue(val totalSeconds: Long) {
    val totalMinutes: Long = totalSeconds / SECONDS_IN_MINUTE
    val justSeconds: Int = (totalSeconds % SECONDS_IN_MINUTE).toInt()
    val justMinutes: Int = ((totalSeconds - justSeconds) / SECONDS_IN_MINUTE)
        .let { totalMinutesLeft ->
            (totalMinutesLeft % MINUTES_IN_HOUR).toInt()
        }
    val justHours: Int =
        (totalSeconds - justMinutes * SECONDS_IN_MINUTE - justSeconds).let { secondsLeft ->
            secondsLeft / (MINUTES_IN_HOUR * SECONDS_IN_MINUTE)
        }.toInt()

    override fun toString(): String = totalSeconds.toString()

    fun format(): String {
        return TimeValueFormatter.formatTimeValue(this)
    }

    fun formatWithoutSeconds(): String {
        return TimeValueFormatter.formatTimeValueWithoutSeconds(this)
    }

    companion object {
        const val MINUTES_IN_HOUR = 60
        const val SECONDS_IN_MINUTE = 60
    }
}

@kotlinx.serialization.Serializable
open class Seconds(private val value: Long) : TimeValue(value) {
    constructor(value: Int) : this(value.toLong())
}

class PositiveSeconds(seconds: Long) : Seconds(abs(seconds)) {
    constructor(timeValue: TimeValue) : this(timeValue.totalSeconds)
    constructor(seconds: Int) : this(seconds.toLong())
}

fun Long.toSeconds() = Seconds(this)
fun Int.toSeconds() = Seconds(this)
fun Int.toMinutes() = Seconds(this * 60)
fun Int.toDays() = Seconds(this * 24 * 60 * 60)
