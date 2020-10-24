package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.HourlyStatistic;
import com.svaboda.storage.stats.StatsDto;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
class MongoWriter implements StatsWriteRepository {

    private final CommandCallsWriter commandCallsWriter;
    private final UniqueChatWriter uniqueChatWriter;

    @Override
    public Try<List<StatsDto>> upsertAll(List<StatsDto> statistics) {
        return Try.of(() -> {
            statistics.forEach(stats -> upsert(HourlyStatistic.from(stats)));
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
                });
    }
}
