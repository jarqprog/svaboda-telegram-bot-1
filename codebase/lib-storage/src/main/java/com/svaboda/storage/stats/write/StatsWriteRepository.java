package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.domain.StatsDto;
import io.vavr.control.Try;

import java.util.List;

public interface StatsWriteRepository {
    Try<List<StatsDto>> upsertAll(List<StatsDto> statistics);
}
