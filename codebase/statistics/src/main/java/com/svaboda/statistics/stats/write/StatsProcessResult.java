package com.svaboda.statistics.stats.write;

import com.svaboda.storage.stats.domain.StatsDto;
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
