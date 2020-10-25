package com.svaboda.storage.stats.domain;

import com.svaboda.utils.TimePeriod;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class StatsFindings {
    TimePeriod.Period forPeriod;
    long totalUsersCount;
    CommandTotalSummary commandTotalSummary;
    List<CommandCalls> commandCalls;
    List<UniqueChat> uniqueChats;

    private StatsFindings(TimePeriod.Period forPeriod, long totalUsersCount, CommandTotalSummary commandTotalSummary,
                          List<CommandCalls> commandCalls, List<UniqueChat> uniqueChats) {
        this.forPeriod = forPeriod;
        this.totalUsersCount = totalUsersCount;
        this.commandTotalSummary = commandTotalSummary;
        this.commandCalls = commandCalls;
        this.uniqueChats = uniqueChats;
    }

    public static StatsFindings create(TimePeriod.Period forPeriod, long totalUsersCount,
                                       CommandTotalSummary commandTotalSummary, List<CommandCalls> commandCalls,
                                       List<UniqueChat> uniqueChats) {
        return new StatsFindings(
                forPeriod,
                totalUsersCount,
                commandTotalSummary,
                commandCalls.stream()
                        .filter(cmdCall -> forPeriod.matches(cmdCall.dateHour()))
                        .collect(Collectors.toList()),
                uniqueChats.stream()
                        .filter(uniqueChat -> forPeriod.matches(uniqueChat.dateHour()))
                        .collect(Collectors.toList())
        );
    }

    public StatsFindings filterBy(TimePeriod.Period period) {
        return create(
                period,
                totalUsersCount,
                commandTotalSummary,
                commandCalls,
                uniqueChats
        );
    }
}
