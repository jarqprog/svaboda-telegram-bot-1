package com.svaboda.statistics.stats.read

import com.svaboda.storage.stats.domain.CommandCalls
import com.svaboda.storage.stats.domain.CommandTotalSummary
import com.svaboda.storage.stats.domain.StatsFindings
import com.svaboda.storage.stats.domain.UniqueChat
import com.svaboda.utils.TimePeriod

object StatsReadUtils {

    private const val ANY_COMMAND_NAME = "command_name"
    private const val ANY_OTHER_COMMAND_NAME = "other_command_name"

    fun lastMonthStatsFindingsWithEmptyMonthPeriod(): StatsFindings {
        val period = TimePeriod.Period.CURRENT_MONTH
        val commandTotalSummary = CommandTotalSummary(
                mapOf(ANY_COMMAND_NAME to 30, ANY_OTHER_COMMAND_NAME to 25),
                55
        )
        val fiveWeeksAgo = TimePeriod.hourFormatNowMinusSec(5 * 7 * 24 * 60 * 60)
        val tenWeeksAgo = TimePeriod.hourFormatNowMinusSec(10 * 7 * 24 * 60 * 60)
        val firstCommandCalls = CommandCalls(fiveWeeksAgo, mapOf(
                ANY_COMMAND_NAME to 3, ANY_OTHER_COMMAND_NAME to 2)
        )
        val secondCommandCalls = CommandCalls(tenWeeksAgo, mapOf(
                ANY_COMMAND_NAME to 1, ANY_OTHER_COMMAND_NAME to 1)
        )
        val commandCalls = listOf(firstCommandCalls, secondCommandCalls)
        val firstUniqueChat = UniqueChat(123L, fiveWeeksAgo)
        val secondUniqueChat = UniqueChat(233L, tenWeeksAgo)
        val uniqueChats = listOf(firstUniqueChat, secondUniqueChat)
        return StatsFindings.create(period, 12, commandTotalSummary, commandCalls, uniqueChats)
    }

}
