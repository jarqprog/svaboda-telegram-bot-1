package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.Stats;
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
    public Try<List<Stats>> upsertAll(List<Stats> statistics) {
        return Try.of(() -> {
            statistics.forEach(stats -> upsert(StatisticWrite.from(stats)));
            return statistics;
        })
                .onFailure(failure -> log.error("Error occurred on saving Statistics", failure));
    }

    private void upsert(StatisticWrite statistic) {
        commandCallsWriter.write(statistic.asCommandCalls())
                .peek(__ -> {
                    final var uniqueChats = statistic.asUniqueChats();
                    final var processed = uniqueChatWriter.write(uniqueChats);
                    log.info("Stats saved with observed new unique chats={}", processed);
                });
    }
}
