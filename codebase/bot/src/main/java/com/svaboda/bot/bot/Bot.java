package com.svaboda.bot.bot;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
class Bot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(Bot.class);

    private final BotProperties botProperties;
    private final MessageProcessor messageProcessor;

    public Bot(DefaultBotOptions options, BotProperties botProperties, MessageProcessor messageProcessor) {
        super(options);
        this.botProperties = botProperties;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void onUpdateReceived(Update update) {
        messageProcessor.processWith(update, this)
                .onFailure(failure ->
                        LOG.error("Error occurred on processing message", failure)
                ).get();
    }

    @Override
    public String getBotUsername() {
        return botProperties.name();
    }

    @Override
    public String getBotToken() {
        return botProperties.token();
    }

}
