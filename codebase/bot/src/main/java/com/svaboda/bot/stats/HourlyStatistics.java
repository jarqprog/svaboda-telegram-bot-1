package com.svaboda.bot.stats;

import com.svaboda.bot.commands.Command;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Value
public class HourlyStatistics {
    private static final DateTimeFormatter DATE_HOUR_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");

    String dateHour;
    Map<String,Integer> commandsCalls = Collections.synchronizedMap(new HashMap<>());
    Set<Long> uniqueChats = Collections.synchronizedSet(new HashSet<>());

    private HourlyStatistics(String dateHour, Command command, long chatId) {
        this.dateHour = dateHour;
        this.commandsCalls.put(command.name(), 1);
        this.uniqueChats.add(chatId);
    }

    public static HourlyStatistics create(LocalDateTime timestamp, Command command, long chatId) {
        return new HourlyStatistics(timestamp.format(DATE_HOUR_FORMAT), command, chatId);
    }

    public void register(Command command, long chatId) {
        commandsCalls.putIfAbsent(command.name(), 0);
        commandsCalls.computeIfPresent(command.name(), (__, value) -> value + 1);
        uniqueChats.add(chatId);
    }

    public boolean isFromSameHour(LocalDateTime localDateTime) {
        return dateHour.equals(localDateTime.format(DATE_HOUR_FORMAT));
    }

    public boolean isBefore(LocalDateTime localDateTime) {
        return dateHour.compareTo(localDateTime.format(DATE_HOUR_FORMAT)) < 0;
    }
}
