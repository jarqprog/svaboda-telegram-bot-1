package com.svaboda.statistics.stats;

import com.svaboda.storage.stats.Stats;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.*;

@Value
class StatisticsResponse {

    List<Stats> statistics;

    Optional<LocalDateTime> latest() {
        return statistics.stream()
                .map(Stats::timestamp)
                .max(LocalDateTime::compareTo);
    }
}
