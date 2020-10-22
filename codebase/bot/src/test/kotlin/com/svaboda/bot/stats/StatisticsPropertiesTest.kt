package com.svaboda.bot.stats

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.temporal.ChronoUnit

class StatisticsPropertiesTest {

    @Test
    fun `should throw exception when not handled mode provided`() {
        //given
        val invalidMode = "invalidMode"

        //when then
        assertThrows<Exception> { StatisticsProperties(invalidMode) }
    }

    @Test
    fun `should create statistics properties when proper values provided`() {
        //given
        val minute = "MINUTE"
        val expectedDefaultMode = ChronoUnit.MINUTES.duration

        //when
        val properties = StatisticsProperties(minute)

        //then
        assertThat(properties.aggregationMode()).isEqualTo(expectedDefaultMode)
    }
}