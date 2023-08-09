package com.bogdan801.digitalfarmer.data.util

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault

class DateTimeUtilException(error: String): Exception(error)

fun getCurrentDateTime(): LocalDateTime = Clock.System.now().toLocalDateTime(currentSystemDefault())

fun LocalDateTime.toEpoch(): Long = toInstant(currentSystemDefault()).toEpochMilliseconds()

fun Long.toLocalDateTime(): LocalDateTime
    = Instant.fromEpochMilliseconds(this).toLocalDateTime(currentSystemDefault())


fun LocalDate.toFormattedString() =
    "${dayOfMonth.toString().padStart(2, '0')}.${monthNumber.toString().padStart(2, '0')}.$year"

fun LocalDateTime.toFormattedTime() =
    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

