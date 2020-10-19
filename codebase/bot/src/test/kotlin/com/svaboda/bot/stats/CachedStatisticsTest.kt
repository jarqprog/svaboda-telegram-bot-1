package com.svaboda.bot.stats

import com.svaboda.bot.commands.Command
import com.svaboda.bot.commands.CommandTestUtils.commandsProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class CachedStatisticsTest {

    private lateinit var cachedStatistics: CachedStatistics

    @BeforeEach
    fun setup() {
        cachedStatistics = CachedStatistics()
    }

    @Test
    fun `should return empty statistics collection when there was no calls against any command`() {
        //given

        //when
        val result = cachedStatistics.provide().get()

        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should collect statistics when there was call against command`() {
        //given
        val chatId = 1L
        val command = Command.TOPICS_INSTANCE
        cachedStatistics.register(command, chatId)

        //when
        val result = cachedStatistics.provide().get()

        //then
        assertThat(result.size).isOne()
        val statistics = result.first()
        assertThat(statistics.uniqueChats().size).isOne()
        assertThat(statistics.uniqueChats()).contains(chatId)
        assertThat(statistics.commandsCalls().size).isOne()
        assertThat(statistics.commandsCalls().containsKey(command.name())).isTrue()
        assertThat(statistics.commandsCalls()[command.name()]).isOne()
    }

    @Test
    fun `should return proper hourly statistics when there were multiple calls against commands`() {
        //given
        val chatId = 1L
        val commands = commandsProperties().commands()
        val commandsCount = mutableMapOf<Command,Int>()
        val random = Random()
        val maxCalls = 100
        commands.forEach { commandsCount[it] = random.nextInt(maxCalls) }
        commandsCount.forEach { (command, count) ->
            for(call in 1..count) { cachedStatistics.register(command, chatId) }
        }

        //when
        val result = cachedStatistics.provide().get()

        //then
        assertThat(result.size).isOne()
        val hourlyStatistics = result.first()
        assertThat(hourlyStatistics.uniqueChats()).isEqualTo(setOf(chatId))
        commandsCount.forEach { (command, count) ->
            assertThat(hourlyStatistics.commandsCalls().containsKey(command.name())).isTrue()
            assertThat(hourlyStatistics.commandsCalls()[command.name()]).isEqualTo(count)
        }
    }

    @Test
    fun `should return the same statistics on multiple provide calls when there was no new registration between calls`() {
        //given
        val chatId = 1L
        val commands = commandsProperties().commands()
        commands.forEach { cachedStatistics.register(it, chatId) }

        //when
        val firstResult = cachedStatistics.provide().get()
        val secondResult = cachedStatistics.provide().get()

        //then
        assertThat(firstResult).isEqualTo(secondResult)
    }

    @Test
    fun `should collect statistics for multiple chats`() {
        //given
        val chatIds = listOf(1L, 2L, 1L, 3L, 1L, 3L)
        val command = Command.TOPICS_INSTANCE
        val expectedCommandCallsCount = mutableMapOf<String,Int>()
        chatIds.forEach { chatId ->
            for(call in 1..10) {
                cachedStatistics.register(command, chatId)
                expectedCommandCallsCount.computeIfPresent(command.name()) { _, count -> count + 1 }
                expectedCommandCallsCount.computeIfAbsent(command.name()) { 1 }
            }
        }
        val expectedUniqueChatsSize = 3

        //when
        val result = cachedStatistics.provide().get()

        //then
        assertThat(result.size).isOne()
        val hourlyStatistics = result.first()
        assertThat(hourlyStatistics.uniqueChats().size).isEqualTo(expectedUniqueChatsSize)
        assertThat(hourlyStatistics.uniqueChats()).containsAll(chatIds)
        assertThat(hourlyStatistics.commandsCalls()).isEqualTo(expectedCommandCallsCount)
    }

    @Test
    fun `should not delete current statistics when past date provided`() {
        //given
        val now = LocalDateTime.now()
        val chatId = 1L
        val commands = commandsProperties().commands()
        commands.forEach { cachedStatistics.register(it, chatId) }
        val statistics = cachedStatistics.provide().get()

        //when
        cachedStatistics.deleteBefore(now.minusHours(2)).get()

        //then
        assertThat(cachedStatistics.provide().get()).isNotEmpty()
        assertThat(cachedStatistics.provide().get()).isEqualTo(statistics)
    }

    @Test
    fun `should delete statistics generated before given time when there was no registration in the meantime`() {
        //given
        val now = LocalDateTime.now()
        val chatId = 1L
        val commands = commandsProperties().commands()
        commands.forEach { cachedStatistics.register(it, chatId) }

        //when
        cachedStatistics.deleteBefore(now.plusHours(2)).get()

        //then
        assertThat(cachedStatistics.provide().get()).isEmpty()
    }
}