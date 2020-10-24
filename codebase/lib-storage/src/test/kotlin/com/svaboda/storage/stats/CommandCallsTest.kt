package com.svaboda.storage.stats

import com.svaboda.storage.stats.StatsUtils.ANY_COMMAND_NAME
import com.svaboda.storage.stats.StatsUtils.ANY_DATE_HOUR
import com.svaboda.storage.stats.StatsUtils.ANY_OTHER_COMMAND_NAME
import com.svaboda.storage.stats.StatsUtils.ANY_OTHER_DATE_HOUR
import com.svaboda.storage.stats.domain.CommandCalls
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CommandCallsTest {

    @Test
    fun `should merge with other when DateHour is the same`() {
        //given
        val commandCallsDocument =mapOf(
                ANY_COMMAND_NAME to 5,
                ANY_OTHER_COMMAND_NAME to 1
        )
        val commandCall = CommandCalls(ANY_DATE_HOUR, commandCallsDocument)

        val other = CommandCalls(ANY_DATE_HOUR, mapOf(ANY_COMMAND_NAME to 10))

        //when
        val result = commandCall.merge(other)

        //then
        assertThat(result.dateHour()).isEqualTo(ANY_DATE_HOUR)
        assertThat(result.commandCalls()[ANY_COMMAND_NAME]).isEqualTo(15)
        assertThat(result.commandCalls()[ANY_OTHER_COMMAND_NAME]).isEqualTo(1)
    }

    @Test
    fun `should throw exception on merge when DateHour is not the same`() {
        //given
        val commandCall = CommandCalls(ANY_DATE_HOUR, mapOf(ANY_COMMAND_NAME to 1))
        val other = CommandCalls(ANY_OTHER_DATE_HOUR, mapOf(ANY_COMMAND_NAME to 10))

        //when then
        assertThrows<IllegalArgumentException> { commandCall.merge(other) }
    }

}