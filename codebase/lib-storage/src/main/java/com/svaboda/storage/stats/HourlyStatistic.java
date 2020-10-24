package com.svaboda.storage.stats;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.svaboda.storage.stats.StatsDb.DATE_HOUR_FORMAT;

@Value
public class HourlyStatistic {

    String dateHour;
    Map<String,Integer> commandsCalls;
    Set<Long> uniqueChats;

    public static HourlyStatistic from(StatsDto statsDto) {
        return new HourlyStatistic(
                LocalDateTime.from(statsDto.timestamp()).format(DATE_HOUR_FORMAT),
                new HashMap<>(statsDto.commandsCalls()),
                statsDto.uniqueChats()
        );
    }

    public CommandCalls asCommandCalls() {
        return new CommandCalls(
                this.dateHour(),
                new HashMap<>(commandsCalls)
        );
    }

    public Set<UniqueChat> asUniqueChats() {
        return uniqueChats.stream()
                .map(chatId -> new UniqueChat(chatId, dateHour))
                .collect(Collectors.toSet());
    }
}
