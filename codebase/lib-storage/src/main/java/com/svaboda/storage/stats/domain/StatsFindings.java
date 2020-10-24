package com.svaboda.storage.stats.domain;

import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class StatsFindings {
    StatsPeriod.Period forPeriod;
    long totalUsersCount;
    CommandTotalSummary commandTotalSummary;
    List<CommandCalls> commandCalls;
    List<UniqueChat> uniqueChats;

    public StatsFindings filterBy(StatsPeriod.Period period) {
        return new StatsFindings(
                period,
                totalUsersCount,
                commandTotalSummary,
                commandCalls.stream()
                        .filter(cmdCall -> cmdCall.dateHour().startsWith(period.searchFilter()))
                        .collect(Collectors.toList()),
                uniqueChats.stream()
                        .filter(uniqueChat -> uniqueChat.dateHour().startsWith(period.searchFilter()))
                        .collect(Collectors.toList())
        );
    }
}
