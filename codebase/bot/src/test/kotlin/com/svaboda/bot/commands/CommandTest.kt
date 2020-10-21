package com.svaboda.bot.commands

import com.svaboda.bot.commands.CommandTestUtils.ANY_EXTERNAL_LINK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CommandTest {

    @Test
    fun `should not create command with empty values`() {
        //given
        val invalidParams = listOf(null, "", " ")

        //then
        invalidParams.forEach {
            assertThrows<IllegalArgumentException> { Command(it, it, it) }
        }
    }

    @Test
    fun `should return false when command is not topics command`() {
        //given
        val command = Command("any", "any", ANY_EXTERNAL_LINK)

        //when
        val result = command.isTopicsCommand

        //then
        assertThat(result).isFalse
    }

}
