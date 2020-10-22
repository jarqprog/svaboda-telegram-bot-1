package com.svaboda.bot.stats;

import io.vavr.control.Try;

import java.util.List;

public interface StatisticsProvider {
    Try<List<Statistics>> provide();
}
