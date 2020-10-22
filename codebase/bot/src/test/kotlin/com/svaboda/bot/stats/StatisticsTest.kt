package com.svaboda.bot.stats

import com.svaboda.bot.commands.CommandTestUtils.cyrillicCommand
import com.svaboda.bot.commands.CommandTestUtils.topicsCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class StatisticsTest {

    @Test
    fun `should create statistics`() {
        //given
        val timestamp = LocalDateTime.now()
        val chatId = 22L
        val command = topicsCommand()

        //when
        val result = Statistics.create(timestamp, command, chatId)

        //then
        assertThat(result.timestamp()).isEqualTo(timestamp)
        assertThat(result.uniqueChats()).isEqualTo(setOf(chatId))
        assertThat(result.commandsCalls()).isEqualTo(mapOf(Pair(command.name(), 1)))
    }

    @Test
    fun `should register command calls`() {
        //given
        val now = LocalDateTime.now()
        val command = topicsCommand()
        val chatId = 1L

        //when
        val statistics = Statistics.create(now, command, chatId)
        statistics.register(command, chatId)
        statistics.register(command, chatId)

        //then
        assertThat(statistics.commandsCalls().size).isOne()
        assertThat(statistics.commandsCalls()[command.name()]).isEqualTo(3)
        assertThat(statistics.uniqueChats()).isEqualTo(setOf(chatId))
    }

    @Test
    fun `should register command calls with unique chatIds`() {
        //given
        val now = LocalDateTime.now()
        val command = topicsCommand()
        val anotherCommand = cyrillicCommand()
        val chatId = 1L
        val anotherChatId = 2L

        //when
        val hourlyStatistics = Statistics.create(now, command, chatId)
        hourlyStatistics.register(command, anotherChatId)
        hourlyStatistics.register(anotherCommand, anotherChatId)

        //then
        assertThat(hourlyStatistics.commandsCalls().size).isEqualTo(2)
        assertThat(hourlyStatistics.commandsCalls()[command.name()]).isEqualTo(2)
        assertThat(hourlyStatistics.commandsCalls()[anotherCommand.name()]).isEqualTo(1)
        assertThat(hourlyStatistics.uniqueChats()).isEqualTo(setOf(chatId, anotherChatId))
    }
}