package com.spqrta.state.common.api

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

object DateTime {

    val dateNow: LocalDate
        get() = LocalDate.now()

    val dateTimeNow: LocalDateTime
        get() = LocalDateTime.now()

}
