package com.svaboda.storage.stats.domain;

import lombok.Value;

import java.util.List;

@Value
public class StatsFindings {
    StatsPeriod.Period period;
    long totalUsersCount;
    CommandTotalSummary commandTotalSummary;
    List<CommandCalls> commandCalls;
    List<UniqueChat> uniqueChats;
}
