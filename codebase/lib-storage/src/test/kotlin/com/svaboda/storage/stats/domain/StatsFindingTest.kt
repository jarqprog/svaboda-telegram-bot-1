package com.svaboda.storage.stats.domain

import com.svaboda.storage.stats.StatsUtils.ANY_COMMAND_NAME
import com.svaboda.storage.stats.StatsUtils.ANY_OTHER_COMMAND_NAME
import com.svaboda.storage.stats.domain.StatsPeriod.hourFormat
import com.svaboda.utils.UnifiedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StatsFindingTest {

    private val totalUsersCount = 10L
    private val commandTotalSummary = CommandTotalSummary(
            mapOf(ANY_COMMAND_NAME to 30, ANY_OTHER_COMMAND_NAME to 25),
            55
    )

    @Test
    fun `should create findings containing last month results with totals summary`() {
        //given
        val period = StatsPeriod.Period.CURRENT_MONTH
        val now = hourFormat(UnifiedDateTime.now())
        val twoMonthsAgo = hourFormat(UnifiedDateTime.now().minusMonths(2))
        val currentCommandCall = CommandCalls(now, mapOf(
                ANY_COMMAND_NAME to 3, ANY_OTHER_COMMAND_NAME to 2)
        )
        val commandCallFromTwoMonthAgo = CommandCalls(twoMonthsAgo, mapOf(
                ANY_COMMAND_NAME to 1, ANY_OTHER_COMMAND_NAME to 1)
        )
        val commandCalls = listOf(currentCommandCall, commandCallFromTwoMonthAgo)
        val currentUniqueChat = UniqueChat(123L, now)
        val uniqueChatFromTwoMonthsAgo = UniqueChat(233L, twoMonthsAgo)
        val uniqueChats = listOf(currentUniqueChat, uniqueChatFromTwoMonthsAgo)

        //when
        val result = StatsFindings
                .createWith(period, totalUsersCount, commandTotalSummary, commandCalls, uniqueChats)

        //then
        assertThat(result.forPeriod()).isEqualTo(period)
        assertThat(result.totalUsersCount()).isEqualTo(totalUsersCount)
        assertThat(result.commandTotalSummary()).isEqualTo(commandTotalSummary)

        assertThat(result.commandCalls()).isEqualTo(listOf(currentCommandCall))
        assertThat(result.uniqueChats()).isEqualTo(listOf(currentUniqueChat))
    }

    @Test
    fun `should create findings containing previous hour results with totals summary`() {
        //given
        val now = hourFormat(UnifiedDateTime.now())
        val eightyMinutesAgo = hourFormat(UnifiedDateTime.now().minusMinutes(80))
        val commandCallFromEightyMinutesAgo = CommandCalls(eightyMinutesAgo, mapOf(
                ANY_COMMAND_NAME to 1, ANY_OTHER_COMMAND_NAME to 1)
        )
        val currentCommandCall = CommandCalls(now, mapOf(
                ANY_COMMAND_NAME to 3, ANY_OTHER_COMMAND_NAME to 2)
        )
        val commandCalls = listOf(commandCallFromEightyMinutesAgo, currentCommandCall)

        val uniqueChatFromEightyMinutesAgo = UniqueChat(233L, eightyMinutesAgo)
        val currentUniqueChat = UniqueChat(123L, now)
        val uniqueChats = listOf(uniqueChatFromEightyMinutesAgo, currentUniqueChat)

        //when
        val result = StatsFindings
                .createWith(StatsPeriod.Period.PREVIOUS_HOUR, totalUsersCount, commandTotalSummary, commandCalls,
                        uniqueChats)

        //then
        assertThat(result.forPeriod()).isEqualTo(StatsPeriod.Period.PREVIOUS_HOUR)
        assertThat(result.totalUsersCount()).isEqualTo(totalUsersCount)
        assertThat(result.commandTotalSummary()).isEqualTo(commandTotalSummary)

        assertThat(result.commandCalls()).isEqualTo(listOf(commandCallFromEightyMinutesAgo))
        assertThat(result.uniqueChats()).isEqualTo(listOf(uniqueChatFromEightyMinutesAgo))
    }

    @Test
    fun `should produce findings containing current hour results with totals summary`() {
        //given
        val now = hourFormat(UnifiedDateTime.now())
        val twoHoursAgo = hourFormat(UnifiedDateTime.now().minusHours(2))
        val previousDay = hourFormat(UnifiedDateTime.now().minusDays(1))
        val currentCommandCall = CommandCalls(now, mapOf(
                ANY_COMMAND_NAME to 3, ANY_OTHER_COMMAND_NAME to 2)
        )
        val commandCallFromTwoHoursAgo = CommandCalls(twoHoursAgo, mapOf(
                ANY_COMMAND_NAME to 1, ANY_OTHER_COMMAND_NAME to 1)
        )
        val commandCallFromPreviousDay = CommandCalls(previousDay, mapOf(
                ANY_COMMAND_NAME to 4, ANY_OTHER_COMMAND_NAME to 3)
        )
        val commandCalls = listOf(currentCommandCall, commandCallFromTwoHoursAgo, commandCallFromPreviousDay)
        val currentUniqueChat = UniqueChat(123L, now)
        val uniqueChatFromTwoHoursAgo = UniqueChat(233L, twoHoursAgo)
        val uniqueChatFromPreviousDay = UniqueChat(235L, previousDay)
        val uniqueChats = listOf(currentUniqueChat, uniqueChatFromTwoHoursAgo, uniqueChatFromPreviousDay)
        val currentMonthsFindings = StatsFindings
                .createWith(StatsPeriod.Period.CURRENT_MONTH, totalUsersCount, commandTotalSummary, commandCalls,
                        uniqueChats)

        //when
        val currentHourFindings = currentMonthsFindings.filterBy(StatsPeriod.Period.CURRENT_HOUR)

        //then
        assertThat(currentHourFindings.forPeriod()).isEqualTo(StatsPeriod.Period.CURRENT_HOUR)
        assertThat(currentHourFindings.totalUsersCount()).isEqualTo(totalUsersCount)
        assertThat(currentHourFindings.commandTotalSummary()).isEqualTo(commandTotalSummary)

        assertThat(currentHourFindings.commandCalls()).isEqualTo(listOf(currentCommandCall))
        assertThat(currentHourFindings.uniqueChats()).isEqualTo(listOf(currentUniqueChat))
    }

}
