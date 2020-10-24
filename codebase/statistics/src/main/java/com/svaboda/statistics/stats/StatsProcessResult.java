package com.svaboda.statistics.stats;

import com.svaboda.storage.stats.StatsDto;
import io.vavr.control.Try;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.*;

@Value
class StatsProcessResult {

    Try<List<StatsDto>> processed;

    Try<Optional<LocalDateTime>> latest() {
        return processed
                .map(stats -> stats.stream()
                        .map(StatsDto::timestamp)
                        .max(LocalDateTime::compareTo)
                );
    }
}
