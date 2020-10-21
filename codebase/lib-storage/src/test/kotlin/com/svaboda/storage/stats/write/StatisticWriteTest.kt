package com.svaboda.storage.stats.write

import com.svaboda.storage.stats.StatsUtils.ANY_COMMAND_NAME
import com.svaboda.storage.stats.StatsUtils.ANY_DATE_HOUR
import org.assertj.core.api.Assertions.assertThat
import org.bson.Document
import org.junit.jupiter.api.Test
import java.util.stream.Collectors

class StatisticWriteTest {

    @Test
    fun `should create commandCalls`() {
        //given
        val chatId = 100L
        val commandCalls = mapOf(ANY_COMMAND_NAME to 1)
        val statisticWrite = StatisticWrite(ANY_DATE_HOUR, commandCalls, setOf(chatId))

        //when
        val result = statisticWrite.asCommandCalls()

        //then
        assertThat(result.commandCalls()).isEqualTo(Document(commandCalls))
        assertThat(result.dateHour()).isEqualTo(ANY_DATE_HOUR)
    }

    @Test
    fun `should create uniqueChats`() {
        //given
        val chatIds = setOf(123L, 210L, 321L)
        val statisticWrite = StatisticWrite(ANY_DATE_HOUR, mapOf(ANY_COMMAND_NAME to 10), chatIds)
        val expectedResult = chatIds.stream()
                .map { chatId -> UniqueChat(chatId, ANY_DATE_HOUR) }
                .collect(Collectors.toSet())

        //when
        val result = statisticWrite.asUniqueChats()

        //then
        assertThat(result).isEqualTo(expectedResult)
    }


}