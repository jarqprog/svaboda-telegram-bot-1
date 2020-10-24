package com.svaboda.bot.stats;

import com.svaboda.bot.commands.Command;
import com.svaboda.utils.UnifiedDateTime;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class CachedStatistics implements StatisticsHandler {

    private final Duration aggregationTimeWindow;
    private final Stack<Statistics> inAggregation = new Stack<>();
    private final List<Statistics> aggregated = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Try<Void> register(Command command, Long chatId) {
        return Try.run(() -> registerStatistics(command, chatId));
    }

    @Override
    public Try<List<Statistics>> provide() {
        if (!inAggregation.isEmpty()) {
            aggregated.add(inAggregation.pop());
        }
        return Try.of(() -> new ArrayList<>(aggregated));
    }

    @Override
    public Try<Void> deleteAt(LocalDateTime at) {
        return Try.run(() -> removeFromDate(at))
            .onFailure(ex -> log.error("Error occurred on removing statistics", ex));
    }

    private void registerStatistics(Command command, Long chatId) {
        final var now = UnifiedDateTime.now();
        if (inAggregation.isEmpty()) {
            inAggregation.push(Statistics.create(now, command, chatId));
        } else {
            if (shouldAggregate(now)) {
                inAggregation.peek().register(command, chatId);
            } else {
                aggregated.add(inAggregation.pop());
                inAggregation.push(Statistics.create(now, command, chatId));
            }
        }
    }

    private void removeFromDate(LocalDateTime timestamp) {
        final var toKeep = aggregated.stream()
                .filter(stats -> stats.timestamp().isAfter(timestamp))
                .collect(Collectors.toList());
        aggregated.clear();
        aggregated.addAll(toKeep);
    }

    private boolean shouldAggregate(LocalDateTime now) {
        return inAggregation.peek().timestamp().isAfter(now.minus(aggregationTimeWindow));
    }
}
