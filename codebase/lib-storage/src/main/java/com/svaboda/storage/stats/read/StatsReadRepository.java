package com.svaboda.storage.stats.read;

import com.svaboda.storage.stats.write.StatisticWrite;
import io.vavr.control.Try;

import java.util.List;

public interface StatsReadRepository {
    Try<Void> upsertAll(List<StatisticWrite> statistics);
}
