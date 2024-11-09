package com.spqrta.state.common.environments

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
object DateTimeEnvironment {

    private var debugDateCompensation: Long = 0

    fun bumpDebugDateCompensation() {
        debugDateCompensation += 1
    }

    val dateNow: LocalDate
        get() = if (debugDateCompensation == 0L) {
            LocalDate.now()
        } else {
            LocalDate.now().plusDays(debugDateCompensation)
        }

    val dateTimeNow: LocalDateTime
        get() = if (debugDateCompensation == 0L) {
            LocalDateTime.now()
        } else {
            LocalDateTime.now().plusDays(debugDateCompensation)
        }

    val displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

}
