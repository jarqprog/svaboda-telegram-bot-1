package com.svaboda.bot.stats;

import com.svaboda.bot.commands.Command;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
class CachedStatistics implements StatisticsHandler {

    private final Set<Long> uniqueChats = Collections.synchronizedSet(new HashSet<>());
    private final Map<Command, AtomicLong> commandsCount = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Try<Void> register(Command command, Long chatId) {
        return Try.run(() -> {
            uniqueChats.add(chatId);
            if (commandsCount.containsKey(command)) {
                commandsCount.computeIfPresent(command, (__, count) -> new AtomicLong(count.incrementAndGet()));
                return;
            }
            commandsCount.computeIfAbsent(command, __ -> new AtomicLong(1));
        });
    }

    @Override
    public Try<Statistics> provide() {
        return Try.of(() ->
                commandsCount.entrySet().stream()
                        .map(Statistics.CommandCallCount::from)
                        .collect(Collectors.toList())
        ).map(commandsCount -> new Statistics(uniqueChats.size(), commandsCount));
    }

    @Override
    public Try<Void> delete() {
        return Try.run(() -> {
            uniqueChats.clear();
            commandsCount.clear();
        })
                .peek(__ -> log.info("stats deleted"))
                .onFailure(failure -> log.error("Error occurred on removing stats", failure));
    }
}
