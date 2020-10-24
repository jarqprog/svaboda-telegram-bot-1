package com.svaboda.statistics.stats.write;

import io.vavr.control.Try;

public interface StatsOperation {
    Try<StatsProcessResult> process(String targetServiceUrl);
}
