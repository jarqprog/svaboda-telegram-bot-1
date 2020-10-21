package com.svaboda.storage.stats.write;

import io.vavr.control.Try;

import java.util.List;

public interface StatsWriteRepository {
    Try<Void> upsertAll(List<StatisticWrite> statistics);
}
