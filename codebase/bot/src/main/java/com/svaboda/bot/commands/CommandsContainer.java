package com.svaboda.bot.commands;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class CommandsContainer implements Commands {
    private final static String TELEGRAM_COMMAND_PREFIX = "/";

    private final Map<String, Command> commandsCache = new ConcurrentHashMap<>();

    CommandsContainer(List<Command> commands) {
        commands.forEach(command -> commandsCache
                .computeIfAbsent(TELEGRAM_COMMAND_PREFIX + command.name(), __ -> command)
        );
    }

    @Override
    public Command byName(String commandName) {
        return commandsCache.getOrDefault(commandName, Command.TOPICS_INSTANCE);
    }

}
