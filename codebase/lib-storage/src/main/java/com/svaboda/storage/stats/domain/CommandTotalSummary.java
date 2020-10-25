package com.svaboda.storage.stats.domain;

import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class CommandTotalSummary {
    Map<String, Long> commandCalls;
    long totalCalls;

    public static CommandTotalSummary from(List<CommandCount> commandCounts) {
        final var cmdCalls = new HashMap<String, Long>(commandCounts.size());
        final var total = commandCounts.stream()
                .peek(commandCount -> cmdCalls.putIfAbsent(commandCount.command(), commandCount.count()))
                .map(CommandCount::count)
                .reduce(0L, Long::sum);
        return new CommandTotalSummary(cmdCalls, total);
    }
}
