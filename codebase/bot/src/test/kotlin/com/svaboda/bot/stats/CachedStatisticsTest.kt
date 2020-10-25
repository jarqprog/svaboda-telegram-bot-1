package com.svaboda.bot.stats

import com.svaboda.bot.commands.Command
import com.svaboda.bot.commands.CommandTestUtils.commandsProperties
import com.svaboda.utils.TimePeriod.ServiceDateTime.now
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.temporal.ChronoUnit

class CachedStatisticsTest {

    private lateinit var cachedStatistics: CachedStatistics
    private val aggregationTimeWindow = ChronoUnit.SECONDS.duration

    @BeforeEach
    fun setup() {
        cachedStatistics = CachedStatistics(aggregationTimeWindow)
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

    @Test //todo
    fun `should return proper statistics when there were multiple calls against commands`() {
        //given
        val chatId = 1L
        val commands = commandsProperties().commands()
        val commandsCount = mutableMapOf<Command,Int>()
        val callsNumber = 3
        commands.forEach { commandsCount[it] = callsNumber }
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
    fun `should return the same statistics on multiple provide calls`() {
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
                expectedCommandCallsCount.computeIfAbsent(command.name()) { 0 }
                expectedCommandCallsCount.computeIfPresent(command.name()) { _, count -> count + 1 }
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
    fun `should not delete statistics when past date provided`() {
        //given
        val chatId = 1L
        val commands = commandsProperties().commands()
        commands.forEach { cachedStatistics.register(it, chatId) }
        consumeStats()

        //when
        cachedStatistics.deleteAt(now().minusSeconds(1)).get()

        //then
        assertThat(cachedStatistics.provide().get()).isNotEmpty()
    }

    @Test
    fun `should delete all statistics when future date provided`() {
        //given
        val chatId = 1L
        val commands = commandsProperties().commands()
        commands.forEach { cachedStatistics.register(it, chatId) }
        consumeStats()

        //when
        cachedStatistics.deleteAt(now().plusSeconds(1)).get()

        //then
        assertThat(cachedStatistics.provide().get()).isEmpty()
    }

    private fun consumeStats() = cachedStatistics.provide()
}