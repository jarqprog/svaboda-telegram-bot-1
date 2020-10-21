package com.svaboda.bot.commands

import com.svaboda.bot.commands.CommandTestUtils.ANY_EXTERNAL_LINK
import com.svaboda.bot.commands.CommandTestUtils.TELEGRAM_COMMAND_PREFIX
import com.svaboda.bot.commands.CommandTestUtils.topicsCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CommandsProviderTest {

    @Test
    fun `should return default command when no commands provided on creation`() {
        //given
        val commandsProvider = CommandsContainer(emptyList())

        //when
        val command = commandsProvider.byName(topicsCommand().name())

        //then
        assertThat(command).isEqualTo(topicsCommand())
    }

    @Test
    fun `should return all commands provided on creation`() {
        // given
        val commands = listOf(
                Command("first", "anyFirst", ANY_EXTERNAL_LINK),
                Command("second", "any-second", ANY_EXTERNAL_LINK),
                Command("third", "any-third", ANY_EXTERNAL_LINK)
        )

        //when
        val result = CommandsContainer(commands)

        //then
        commands.forEach {
            assertThat(result.byName(TELEGRAM_COMMAND_PREFIX + it.name())).isEqualTo(it)
        }
    }
}