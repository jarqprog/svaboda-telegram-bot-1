package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.domain.HourlyStatistic;
import com.svaboda.storage.stats.domain.StatsDto;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class MongoWriter implements StatsWriteRepository {

    private final CommandCountWriter commandCountWriter;
    private final CommandCallsWriter commandCallsWriter;
    private final UniqueChatWriter uniqueChatWriter;

    @Override
    public Try<List<StatsDto>> upsertAll(List<StatsDto> statistics) {
        return Try.of(() -> {

            final var hourlyStatistics = statistics.stream()
                    .map(HourlyStatistic::from)
                    .collect(Collectors.toList());
            commandCountWriter.write(hourlyStatistics)
                    .peek(hourlyStats -> hourlyStatistics.forEach(this::upsert))
                    .get();
            return statistics;
        })
                .onFailure(failure -> log.error("Error occurred on saving Statistics", failure));
    }

    private void upsert(HourlyStatistic statistic) {
        commandCallsWriter.write(statistic.asCommandCalls())
                .peek(__ -> {
                    final var uniqueChats = statistic.asUniqueChats();
                    final var processed = uniqueChatWriter.write(uniqueChats);
                    log.info("Stats saved with observed new unique chats={}", processed);
                })
                .get();
    }

}
