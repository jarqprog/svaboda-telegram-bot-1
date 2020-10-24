package com.svaboda.storage.stats.read;

import com.svaboda.storage.stats.domain.StatsFindings;
import io.vavr.control.Try;

public interface StatsReadRepository {
    Try<StatsFindings> fromLastMonth();
}
