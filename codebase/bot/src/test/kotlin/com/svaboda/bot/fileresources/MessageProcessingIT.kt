package com.svaboda.bot.fileresources

import com.svaboda.bot.bot.MessageProcessor
import com.svaboda.bot.commands.CommandTestUtils.TELEGRAM_COMMAND_PREFIX
import com.svaboda.bot.commands.CommandTestUtils.cyrillicCommand
import com.svaboda.bot.commands.CommandTestUtils.topicsCommand
import com.svaboda.bot.commands.CommandsConfiguration
import com.svaboda.bot.commands.CommandsProperties
import com.svaboda.bot.domain.ResourcesProperties
import com.svaboda.bot.fileresources.FileResourcesUtils.resourceProperties
import com.svaboda.bot.fileresources.FileResourcesUtils.topicsContent
import com.svaboda.bot.stats.StatisticsConfiguration
import com.svaboda.bot.stats.StatisticsHandler
import com.svaboda.bot.stats.StatisticsProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

class MessageProcessingIT {

    private lateinit var statisticsConfiguration: StatisticsConfiguration
    private lateinit var statisticsHandler: StatisticsHandler

    private lateinit var simpleMessageProcessor: MessageProcessor

    private lateinit var message: Message
    private lateinit var update: Update
    private lateinit var telegramBot: TelegramLongPollingBot

    @BeforeEach
    fun setup() {
        val commandsProperties = CommandsProperties(listOf(topicsCommand(), cyrillicCommand()))
        val commands = CommandsConfiguration().commands(commandsProperties)
        val resourceProperties: ResourcesProperties = resourceProperties()
        val resourceProvider = CachedFileResourceProvider(
                TextFileResourceReader(resourceProperties), TextTransformer(resourceProperties, commandsProperties)
        )
        val statisticsAggregationTime = "SECOND"
        statisticsConfiguration = StatisticsConfiguration()
        statisticsHandler = statisticsConfiguration.statisticsHandler(StatisticsProperties(statisticsAggregationTime))
        simpleMessageProcessor = SimpleMessageProcessor(resourceProvider, statisticsHandler, commands)

        message = Mockito.mock(Message::class.java)
        update = Mockito.mock(Update::class.java)
        telegramBot = Mockito.mock(TelegramLongPollingBot::class.java)
    }

    @Test
    fun `should successfully process message when no failure on underlying telegram dependency occurred`() {
        //given
        val command = topicsCommand()
        val chatId = 1L
        val sendMessage = SendMessage(chatId, topicsContent())
        Mockito.`when`(message.text).thenReturn(TELEGRAM_COMMAND_PREFIX + command.name())
        Mockito.`when`(message.chatId).thenReturn(chatId)
        Mockito.`when`(update.message).thenReturn(message)
        Mockito.`when`(telegramBot.execute(sendMessage)).thenReturn(Mockito.mock(Message::class.java))

        //when
        val result = simpleMessageProcessor.processWith(update, telegramBot)

        //then
        assertThat(result.isSuccess).isTrue
    }

    @Test
    fun `should return default resource when asked with unknown command`() {
        //given
        val commandName = "unknown"
        val chatId = 1L
        val expectedMessageToSend = SendMessage(chatId, topicsContent())
        Mockito.`when`(message.text).thenReturn(commandName)
        Mockito.`when`(message.chatId).thenReturn(chatId)
        Mockito.`when`(update.message).thenReturn(message)

        //when
        simpleMessageProcessor.processWith(update, telegramBot)

        //then
        Mockito.verify(telegramBot, Mockito.times(1)).execute(expectedMessageToSend)
    }

    @Test
    fun `should handle underlying telegram failure response`() {
        //given
        val command = topicsCommand()
        val chatId = 1L
        val sendMessage = SendMessage(chatId, topicsContent())
        val terribleError = TelegramApiException("Boom!")
        Mockito.`when`(message.text).thenReturn(TELEGRAM_COMMAND_PREFIX + command.name())
        Mockito.`when`(message.chatId).thenReturn(chatId)
        Mockito.`when`(update.message).thenReturn(message)
        Mockito.`when`(telegramBot.execute(sendMessage)).thenThrow(terribleError)

        //when
        val result = simpleMessageProcessor.processWith(update, telegramBot)

        //then
        assertThat(result.isFailure).isTrue
    }

    @Test
    fun `should store statistics while processing messages`() {
        //given
        val command = topicsCommand()
        val chatId = 1L
        val sendMessage = SendMessage(chatId, topicsContent())
        Mockito.`when`(message.text).thenReturn(TELEGRAM_COMMAND_PREFIX + command.name())
        Mockito.`when`(message.chatId).thenReturn(chatId)
        Mockito.`when`(update.message).thenReturn(message)
        Mockito.`when`(telegramBot.execute(sendMessage)).thenReturn(Mockito.mock(Message::class.java))
        val callsNumber = 10

        for (call in 1..callsNumber) { simpleMessageProcessor.processWith(update, telegramBot) }

        //when
        val result = statisticsHandler.provide().get()

        //then
        assertThat(result.size).isOne()
        assertThat(result.first().commandsCalls()).isEqualTo(mapOf(command.name() to callsNumber))
        assertThat(result.first().uniqueChats()).isEqualTo(setOf(chatId))
    }

}
