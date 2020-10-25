package com.svaboda.storage.stats.domain

import com.svaboda.storage.stats.domain.StatsPeriod.hourFormat
import com.svaboda.utils.UnifiedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.streams.toList

class StatsPeriodTest {

    @Test
    fun `should filter hourly formatted dates from current month`() {
        //given
        val twoMonthsAgo = hourFormat(UnifiedDateTime.now().minusMonths(2))
        val now = hourFormat(UnifiedDateTime.now())
        val hourlyDates = listOf(twoMonthsAgo, now)
        val expectedResult = listOf(now)

        //when
        val result = hourlyDates.stream()
                .filter { hourlyDate -> StatsPeriod.Period.CURRENT_MONTH.matches(hourlyDate) }
                .toList()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should filter hourly formatted dates from current day`() {
        //given
        val today = hourFormat(UnifiedDateTime.now())
        val yesterday = hourFormat(UnifiedDateTime.now().minusDays(1))
        val hourlyDates = listOf(today, yesterday)
        val expectedResult = listOf(today)

        //when
        val result = hourlyDates.stream()
                .filter { hourlyDate -> StatsPeriod.Period.TODAY.matches(hourlyDate) }
                .toList()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should filter hourly formatted dates from previous hour`() {
        //given
        val hourBefore = hourFormat(UnifiedDateTime.now().minusMinutes(61))
        val twoHoursBefore = hourFormat(UnifiedDateTime.now().minusMinutes(121))
        val hourlyDates = listOf(hourBefore, twoHoursBefore)
        val expectedResult = listOf(hourBefore)

        //when
        val result = hourlyDates.stream()
                .filter { hourlyDate -> StatsPeriod.Period.PREVIOUS_HOUR.matches(hourlyDate) }
                .toList()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should filter hourly formatted dates from current hour`() {
        //given
        val currentHour = hourFormat(UnifiedDateTime.now())
        val hourBefore = hourFormat(UnifiedDateTime.now().minusMinutes(61))
        val hourlyDates = listOf(currentHour, hourBefore)
        val expectedResult = listOf(currentHour)

        //when
        val result = hourlyDates.stream()
                .filter { hourlyDate -> StatsPeriod.Period.CURRENT_HOUR.matches(hourlyDate) }
                .toList()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

}
