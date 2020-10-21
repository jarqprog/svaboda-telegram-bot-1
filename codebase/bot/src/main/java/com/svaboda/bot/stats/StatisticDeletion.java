package com.svaboda.bot.stats;

import io.vavr.control.Try;

public interface StatisticDeletion {
    Try<Void> delete();
}
