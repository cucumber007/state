package com.spqrta.state.common.util

import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
open class TimeValue(val totalSeconds: Long) {
    val seconds: Int = (totalSeconds % SECONDS_IN_MINUTE).toInt()
    val minutes: Int = ((totalSeconds - seconds) / SECONDS_IN_MINUTE)
        .let { totalMinutesLeft ->
            (totalMinutesLeft % MINUTES_IN_HOUR).toInt()
        }
    val hours: Int =
        (totalSeconds - minutes * SECONDS_IN_MINUTE - seconds).let { secondsLeft ->
            secondsLeft / (MINUTES_IN_HOUR * SECONDS_IN_MINUTE)
        }.toInt()

    override fun toString(): String = totalSeconds.toString()

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
}

fun Long.toSeconds() = Seconds(this)
fun Int.toSeconds() = Seconds(this)
fun Int.toMinutes() = Seconds(this * 60)
