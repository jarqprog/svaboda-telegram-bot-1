package com.svaboda.statistics.stats.read

import com.svaboda.statistics.stats.read.StatsReadUtils.lastMonthStatsFindingsWithEmptyMonthPeriod
import com.svaboda.utils.TimePeriod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SummaryReportTest {

    @Test
    fun `should create report containing total results when no data from current month is available`() {
        //given
        val periods = listOf(TimePeriod.Period.CURRENT_HOUR, TimePeriod.Period.CURRENT_MONTH)
        val statsFindings = lastMonthStatsFindingsWithEmptyMonthPeriod()

        //when
        val result = SummaryReport.generateFor(periods, statsFindings)

        //then
        assertThat(result.title()).isNotBlank()
        assertThat(result.timeInfo()).isNotBlank()
        assertThat(result.generatedAt()).isNotBlank()
        assertThat(result.totalCalls()).isEqualTo(statsFindings.commandTotalSummary().totalCalls())
        assertThat(result.uniqueUsersCount()).isEqualTo(statsFindings.totalUsersCount())
        assertThat(result.periodReports()).isNotEmpty
    }

    @Test
    fun `should create report containing period reports in specific order according to passed period list`() {
        //given
        val periods = listOf(TimePeriod.Period.CURRENT_MONTH, TimePeriod.Period.TODAY, TimePeriod.Period.PREVIOUS_HOUR,
                TimePeriod.Period.CURRENT_HOUR)
        val statsFindings = lastMonthStatsFindingsWithEmptyMonthPeriod()

        //when
        val result = SummaryReport.generateFor(periods, statsFindings).periodReports()

        //then
        result.onEachIndexed { index, (period, _) ->
            assertThat(period == periods[index]).isTrue()
        }

    }

}