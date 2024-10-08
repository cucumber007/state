package com.spqrta.state.common.environments

import java.time.LocalDate
import java.time.LocalDateTime

object DateTimeEnvironment {

    val dateNow: LocalDate
        get() = LocalDate.now()

    val dateTimeNow: LocalDateTime
        get() = LocalDateTime.now()

}
