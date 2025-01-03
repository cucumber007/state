package com.spqrta.state.common.util

import android.annotation.SuppressLint
import com.spqrta.state.common.environments.DateTimeEnvironment
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object DatetimeUtils {
    fun datetimeFromTimestamp(timestamp: Long?, zoneId: ZoneId): LocalDateTime? {
        return timestamp?.let { datetimeFromTimestamp(it, zoneId) }
    }

    fun datetimeFromTimestamp(timestamp: Long, zoneId: ZoneId): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId)
    }
}

fun String.parseDate(dateTimeFormatter: DateTimeFormatter): LocalDate {
    return LocalDate.parse(this, dateTimeFormatter)
}

fun ZonedDateTime.toLocalDateTimeOnThisZone(): LocalDateTime {
    val zone = ZoneId.systemDefault()
    val zoned = this.withZoneSameInstant(zone)
    return zoned.toLocalDateTime()
}

fun LocalTime.toLocalTimeUtc(): LocalTime {
    val zone = ZoneId.systemDefault()
    val local = ZonedDateTime.ofLocal(
        LocalDateTime.of(DateTimeEnvironment.dateNow, this),
        zone,
        null
    )
    val utc = local.withZoneSameInstant(ZoneId.ofOffset("", ZoneOffset.UTC))
    return utc.toLocalTime()
}

fun LocalTime.utcToLocalTime(): LocalTime {
    val local = ZonedDateTime.ofLocal(
        LocalDateTime.of(DateTimeEnvironment.dateNow, this),
        ZoneId.ofOffset("", ZoneOffset.UTC),
        null
    )
    return local.toLocalDateTimeOnThisZone().toLocalTime()
}

fun LocalDateTime.toIso(): String {
    return ZonedDateTime.of(
        this,
        ZoneId.systemDefault()
    ).toOffsetDateTime().atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
}
