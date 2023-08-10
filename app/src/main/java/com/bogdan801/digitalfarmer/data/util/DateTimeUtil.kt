package com.bogdan801.digitalfarmer.data.util

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault

class DateTimeUtilException(error: String): Exception(error)

fun getCurrentDateTime(): LocalDateTime = Clock.System.now().toLocalDateTime(currentSystemDefault())

fun LocalDateTime.toEpoch(): Long = toInstant(currentSystemDefault()).toEpochMilliseconds()

fun Long.toLocalDateTime(): LocalDateTime
    = Instant.fromEpochMilliseconds(this).toLocalDateTime(currentSystemDefault())

fun LocalDateTime.toFormattedDateString() =
    "${dayOfMonth.toString().padStart(2, '0')}/${(month.ordinal+1).toString().padStart(2, '0')}/${year}"

fun LocalDateTime.toFormattedTimeString() =
    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

