package com.spqrta.state.common.environments

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
object DateTimeEnvironment {

    private var debugDateCompensation: Long = 0

    fun bumpDebugDateCompensation() {
        debugDateCompensation += 1
    }

    val dateNow: LocalDate
        get() = dateTimeNow.toLocalDate()

    val dateTimeNow: LocalDateTime
        get() = if (debugDateCompensation == 0L) {
            LocalDateTime.now()
        } else {
            LocalDateTime.now().plusDays(debugDateCompensation)
        }.let {
            LocalDateTime.now().withHour(22)
        }

    val displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val timeNow: LocalTime
        get() = dateTimeNow.toLocalTime()

}
