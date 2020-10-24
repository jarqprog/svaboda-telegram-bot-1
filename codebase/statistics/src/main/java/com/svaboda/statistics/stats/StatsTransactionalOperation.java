package com.svaboda.statistics.stats;

import com.svaboda.storage.stats.write.StatsWriteRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class StatsTransactionalOperation implements StatsOperation {

    private final StatsWriteRepository statsWriteRepository;
    private final StatsProvider statsProvider;

    @Override
    @Transactional
    public Try<StatsProcessResult> process(String targetServiceUrl) {
        return statsProvider.statsFrom(targetServiceUrl)
                .map(ResponseEntity::getBody)
                .map(statsWriteRepository::upsertAll)
                .map(StatsProcessResult::new);
    }
}
