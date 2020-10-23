package com.svaboda.storage.stats.read;

import io.vavr.control.Try;

public interface StatsReadRepository {
    Try<StatsFindings> fromLastMonth();
}
