package com.spqrta.state.api

import java.time.LocalDate
import java.time.LocalDateTime

object DateTime {

    val dateNow: LocalDate
        get() = LocalDate.now()

    val dateTimeNow: LocalDateTime
        get() = LocalDateTime.now()

}
