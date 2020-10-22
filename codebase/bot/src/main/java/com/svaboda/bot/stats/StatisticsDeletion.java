package com.svaboda.bot.stats;

import io.vavr.control.Try;

import java.time.LocalDateTime;

public interface StatisticsDeletion {
    Try<Void> deleteAt(LocalDateTime at);
}
