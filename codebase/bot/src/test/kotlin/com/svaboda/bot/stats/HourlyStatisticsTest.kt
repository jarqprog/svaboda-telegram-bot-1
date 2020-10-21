package com.svaboda.bot.stats

import com.svaboda.bot.commands.CommandTestUtils.cyrillicCommand
import com.svaboda.bot.commands.CommandTestUtils.topicsCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class HourlyStatisticsTest {

    @Test
    fun `should create statistics with command call and chatId`() {
        //given
        val rawDateTime = "2020-10-19T06:10:20"
        val timestamp = LocalDateTime.parse(rawDateTime)
        val expectedCreatedAt = "2020-10-19:06"
        val chatId = 22L
        val command = topicsCommand()

        //when
        val result = HourlyStatistics.create(timestamp, command, chatId)

        //then
        assertThat(result.dateHour()).isEqualTo(expectedCreatedAt)
        assertThat(result.uniqueChats()).isEqualTo(setOf(chatId))
        assertThat(result.commandsCalls()).isEqualTo(mapOf(Pair(command.name(), 1)))
    }

    @Test
    fun `should return true when statistics is from the same hour`() {
        //given
        val firstRawDateTime = "2020-10-19T06:10:20"
        val first = HourlyStatistics.create(LocalDateTime.parse(firstRawDateTime), topicsCommand(), 1)

        //when
        val result = first.isFromSameHour(LocalDateTime.parse("2020-10-19T06:50:20"))

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when statistics is not from the same hour`() {
        //given
        val firstRawDateTime = "2020-10-19T06:10:20"
        val first = HourlyStatistics.create(LocalDateTime.parse(firstRawDateTime), topicsCommand(), 1)

        //when
        val result = first.isFromSameHour(LocalDateTime.parse("2020-10-19T06:50:20").minusHours(2))

        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when statistics was generated before given time`() {
        //given
        val now = LocalDateTime.now()
        val hourlyStatistics = HourlyStatistics.create(now.minusHours(2), topicsCommand(), 1)

        //when
        val result = hourlyStatistics.isBefore(now)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when statistics was generated at the same time as given time`() {
        //given
        val now = LocalDateTime.now()
        val hourlyStatistics = HourlyStatistics.create(now, topicsCommand(), 1)

        //when
        val result = hourlyStatistics.isBefore(now)

        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when statistics was generated later then given time`() {
        //given
        val now = LocalDateTime.now()
        val hourlyStatistics = HourlyStatistics.create(now.plusHours(2), topicsCommand(), 1)

        //when
        val result = hourlyStatistics.isBefore(now)

        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `should register command calls`() {
        //given
        val now = LocalDateTime.now()
        val command = topicsCommand()
        val chatId = 1L

        //when
        val hourlyStatistics = HourlyStatistics.create(now, command, chatId)
        hourlyStatistics.register(command, chatId)
        hourlyStatistics.register(command, chatId)

        //then
        assertThat(hourlyStatistics.commandsCalls().size).isOne()
        assertThat(hourlyStatistics.commandsCalls()[command.name()]).isEqualTo(3)
        assertThat(hourlyStatistics.uniqueChats()).isEqualTo(setOf(chatId))
    }

    @Test
    fun `should register command calls unique chatIds`() {
        //given
        val now = LocalDateTime.now()
        val command = topicsCommand()
        val anotherCommand = cyrillicCommand()
        val chatId = 1L
        val anotherChatId = 2L

        //when
        val hourlyStatistics = HourlyStatistics.create(now, command, chatId)
        hourlyStatistics.register(command, anotherChatId)
        hourlyStatistics.register(anotherCommand, anotherChatId)

        //then
        assertThat(hourlyStatistics.commandsCalls().size).isEqualTo(2)
        assertThat(hourlyStatistics.commandsCalls()[command.name()]).isEqualTo(2)
        assertThat(hourlyStatistics.commandsCalls()[anotherCommand.name()]).isEqualTo(1)
        assertThat(hourlyStatistics.uniqueChats()).isEqualTo(setOf(chatId, anotherChatId))
    }
}