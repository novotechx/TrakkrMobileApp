package com.novotechx.trakkr.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

object DateTimeUtil {
    fun now(): Long = Clock.System.now().toEpochMilliseconds()

    fun todayStartEpoch(): Long {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date
        return today.atStartOfDayIn(tz).toEpochMilliseconds()
    }

    fun weekStartEpoch(): Long {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date
        val dayOfWeek = today.dayOfWeek.isoDayNumber // Mon=1, Sun=7
        val monday = today.minus(dayOfWeek - 1, DateTimeUnit.DAY)
        return monday.atStartOfDayIn(tz).toEpochMilliseconds()
    }

    fun monthStartEpoch(): Long {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date
        val firstOfMonth = LocalDate(today.year, today.month, 1)
        return firstOfMonth.atStartOfDayIn(tz).toEpochMilliseconds()
    }

    fun yearStartEpoch(): Long {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date
        val firstOfYear = LocalDate(today.year, Month.JANUARY, 1)
        return firstOfYear.atStartOfDayIn(tz).toEpochMilliseconds()
    }

    fun epochToLocalDate(epochMillis: Long): LocalDate {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun epochToTimeString(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return pad2(local.hour) + ":" + pad2(local.minute)
    }

    fun getTimeOfDayPrefix(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val hour = instant.toLocalDateTime(TimeZone.currentSystemDefault()).hour
        return when {
            hour < 6 -> "Night"
            hour < 12 -> "Morning"
            hour < 17 -> "Afternoon"
            hour < 21 -> "Evening"
            else -> "Night"
        }
    }

    fun formatDateRelative(epochMillis: Long): String {
        val date = epochToLocalDate(epochMillis)
        val today = epochToLocalDate(now())
        val yesterday = today.minus(1, DateTimeUnit.DAY)

        return when (date) {
            today -> "Today"
            yesterday -> "Yesterday"
            else -> {
                val dayName = date.dayOfWeek.name.lowercase()
                    .replaceFirstChar { it.uppercase() }
                val monthName = date.month.name.lowercase()
                    .replaceFirstChar { it.uppercase() }
                "$dayName, ${date.dayOfMonth} $monthName"
            }
        }
    }

    private fun pad2(value: Int): String = if (value < 10) "0$value" else value.toString()
}
