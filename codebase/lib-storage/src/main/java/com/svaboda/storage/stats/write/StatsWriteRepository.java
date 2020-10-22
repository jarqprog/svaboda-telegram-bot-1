package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.Stats;
import io.vavr.control.Try;

import java.util.List;

public interface StatsWriteRepository {
    Try<List<Stats>> upsertAll(List<Stats> statistics);
}
