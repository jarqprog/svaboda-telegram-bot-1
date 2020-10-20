package com.svaboda.statistics.stats;

import com.svaboda.storage.stats.StatsRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class StatsTransactionalOperation implements StatsOperation {

    private final StatsRepository statsRepository;
    private final StatsProvider statsProvider;
    private final StatsDeletion statsDeletion;

    @Override
    @Transactional
    public Try<Void> process(String targetServiceUrl) {
        return statsProvider.statsFrom(targetServiceUrl)
                .map(ResponseEntity::getBody)
                .flatMap(statsRepository::upsertAll)
                .flatMap(__ -> statsDeletion.deleteFrom(targetServiceUrl))
                .map(ResponseEntity::getBody);
    }
}
