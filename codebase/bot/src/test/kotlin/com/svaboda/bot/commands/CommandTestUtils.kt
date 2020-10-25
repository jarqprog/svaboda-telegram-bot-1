package com.svaboda.bot.commands

object CommandTestUtils {

    const val ANY_EXTERNAL_LINK = "https://docs.google.com/document/d/1-r8kf1zSx1U9RjKIQEfAdL_TKsltG8dXFleMcU9lxbY/edit"
    const val TELEGRAM_COMMAND_PREFIX = "/"
    fun topicsCommand(): Command = Command.TOPICS_INSTANCE
    fun cyrillicCommand(): Command = Command("cyrillic", "cyrillic", ANY_EXTERNAL_LINK)
    fun hugeCommand(): Command = Command("huge", "huge", ANY_EXTERNAL_LINK)
    fun noWhitespaceCommand(): Command = Command("noWhitespace", "no_whitespace", ANY_EXTERNAL_LINK)
    fun commandsProperties(): CommandsProperties = CommandsProperties(
            listOf(topicsCommand(), cyrillicCommand(), hugeCommand(), noWhitespaceCommand())
    )

}