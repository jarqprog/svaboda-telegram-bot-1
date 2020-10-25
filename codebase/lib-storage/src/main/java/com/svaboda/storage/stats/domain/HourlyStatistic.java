package com.svaboda.storage.stats.domain;

import lombok.Value;

import java.util.*;
import java.util.stream.Collectors;

@Value
public class HourlyStatistic {

    String dateHour;
    Map<String,Integer> commandsCalls;
    Set<Long> uniqueChats;

    public static HourlyStatistic from(StatsDto statsDto) {
        return new HourlyStatistic(
                StatsPeriod.hourFormat(statsDto.timestamp()),
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
