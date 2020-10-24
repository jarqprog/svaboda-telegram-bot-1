package com.svaboda.storage.stats.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CommandTotalSummaryTest {

    @Test
    fun `should create from command count collection`() {
        //given
        val firstCommandCount = CommandCount("first", 10)
        val secondCommandCount = CommandCount("second", 5)
        val expectedResult = CommandTotalSummary(
                mapOf("first" to 10, "second" to 5),
                15
        )

        //when
        val result = CommandTotalSummary.from(listOf(firstCommandCount, secondCommandCount))

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

}