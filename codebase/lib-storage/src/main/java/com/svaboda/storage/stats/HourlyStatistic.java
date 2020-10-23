package com.svaboda.storage.stats;

import lombok.Value;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.svaboda.storage.stats.StatsDb.DATE_HOUR_FORMAT;

@Value
public class HourlyStatistic {

    String dateHour;
    Map<String,Object> commandsCalls;
    Set<Long> uniqueChats;

    public static HourlyStatistic from(Stats stats) {
        return new HourlyStatistic(
                LocalDateTime.from(stats.timestamp()).format(DATE_HOUR_FORMAT),
                new HashMap<>(stats.commandsCalls()),
                stats.uniqueChats()
        );
    }

    public CommandCalls asCommandCalls() {
        return new CommandCalls(
                this.dateHour(),
                new Document(commandsCalls)
        );
    }

    public Set<UniqueChat> asUniqueChats() {
        return uniqueChats.stream()
                .map(chatId -> new UniqueChat(chatId, dateHour))
                .collect(Collectors.toSet());
    }
}
