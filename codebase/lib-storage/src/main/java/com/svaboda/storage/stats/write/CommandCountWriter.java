package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.domain.CommandCount;
import com.svaboda.storage.stats.domain.HourlyStatistic;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
class CommandCountWriter {

    private static final Query FIND_ALL = new Query();
    private final MongoTemplate mongoTemplate;

    Try<List<HourlyStatistic>> write(List<HourlyStatistic> hourlyStatistics) {
        return upsert(hourlyStatistics);
    }

    private Try<List<HourlyStatistic>> upsert(List<HourlyStatistic> hourlyStatistics) {
        return Try.of(() -> {
                if (hourlyStatistics.isEmpty()) {
                    return hourlyStatistics;
                }
                final var existing = loadExisting();
                final var merged = merge(existing, hourlyStatistics);
                mongoTemplate.insert(merged, CommandCount.Entity.class);
                log.info("Updated totals commands count");
                return hourlyStatistics;
            }
        );
    }

    private List<CommandCount> loadExisting() {
        return mongoTemplate.findAllAndRemove(FIND_ALL, CommandCount.Entity.class).stream()
                        .map(CommandCount::from)
                        .collect(Collectors.toList());
    }

    private List<CommandCount> merge(List<CommandCount> existing, List<HourlyStatistic> toUpsert) {
        final var toMerge = new HashMap<String,Long>();
        existing.forEach(commandCount ->
                toMerge.put(commandCount.command(), commandCount.count())
        );
        toUpsert.forEach(hourlyStatistic ->
                hourlyStatistic.commandsCalls().forEach((command,callCount) -> {
                            toMerge.putIfAbsent(command, 0L);
                            toMerge.computeIfPresent(command, (__,cc) -> callCount + cc);
                        }
                )
        );
        final var merged = new ArrayList<CommandCount>(toMerge.size());
        toMerge.forEach((command,count) -> merged.add(new CommandCount(command, count)));
        return merged;
    }
}
