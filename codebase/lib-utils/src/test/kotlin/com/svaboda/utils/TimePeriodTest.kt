package com.svaboda.utils

import com.svaboda.utils.TimePeriod.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.streams.toList

class TimePeriodTest {

    @Test
    fun `should filter hourly formatted dates from current month`() {
        //given
        val fiveWeeksAgo = hourFormatNowMinusSec(5*7*24*60*60)
        val now = hourFormatNow()
        val hourlyDates = listOf(fiveWeeksAgo, now)
        val expectedResult = listOf(now)

        //when
        val result = hourlyDates.stream()
                .filter { hourlyDate -> Period.CURRENT_MONTH.matches(hourlyDate) }
                .toList()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should filter hourly formatted dates from current day`() {
        //given
        val now = hourFormatNow()
        val yesterday = hourFormatNowMinusSec(24*60*60)
        val hourlyDates = listOf(now, yesterday)
        val expectedResult = listOf(now)

        //when
        val result = hourlyDates.stream()
                .filter { hourlyDate -> Period.TODAY.matches(hourlyDate) }
                .toList()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should filter hourly formatted dates from previous hour`() {
        //given
        val hourBefore = hourFormatNowMinusSec(60*60)
        val twoHoursBefore = hourFormatNowMinusSec(2*60*60)
        val hourlyDates = listOf(hourBefore, twoHoursBefore)
        val expectedResult = listOf(hourBefore)

        //when
        val result = hourlyDates.stream()
                .filter { hourlyDate -> Period.PREVIOUS_HOUR.matches(hourlyDate) }
                .toList()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should filter hourly formatted dates from current hour`() {
        //given
        val currentHour = hourFormatNow()
        val hourBefore = hourFormatNowMinusSec(60*60)
        val hourlyDates = listOf(currentHour, hourBefore)
        val expectedResult = listOf(currentHour)

        //when
        val result = hourlyDates.stream()
                .filter { hourlyDate -> Period.CURRENT_HOUR.matches(hourlyDate) }
                .toList()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

}
