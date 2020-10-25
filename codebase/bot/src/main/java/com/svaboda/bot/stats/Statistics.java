package com.svaboda.bot.stats;

import com.svaboda.bot.commands.Command;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.*;

@Value
public class Statistics {

    LocalDateTime timestamp;
    Map<String, Integer> commandsCalls = Collections.synchronizedMap(new HashMap<>());
    Set<Long> uniqueChats = Collections.synchronizedSet(new HashSet<>());

    private Statistics(LocalDateTime timestamp, Command command, long chatId) {
        this.timestamp = timestamp;
        this.commandsCalls.put(command.name(), 1);
        this.uniqueChats.add(chatId);
    }

    public static Statistics create(LocalDateTime timestamp, Command command, long chatId) {
        return new Statistics(timestamp, command, chatId);
    }

    public void register(Command command, long chatId) {
        commandsCalls.putIfAbsent(command.name(), 0);
        commandsCalls.computeIfPresent(command.name(), (__, value) -> value + 1);
        uniqueChats.add(chatId);
    }
}
