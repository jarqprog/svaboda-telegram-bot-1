package com.svaboda.statistics.stats;

import com.svaboda.storage.stats.write.StatsWriteRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class StatsTransactionalOperation implements StatsOperation {

    private final StatsWriteRepository statsWriteRepository;
    private final StatsProvider statsProvider;
    private final StatsDeletion statsDeletion;

    @Override
    @Transactional
    public Try<Void> process(String targetServiceUrl) {
        return statsProvider.statsFrom(targetServiceUrl)
                .map(ResponseEntity::getBody)
                .flatMap(statsWriteRepository::upsertAll)
                .map(StatisticsResponse::new)
                .flatMap(statisticsResponse ->
                    statisticsResponse.latest()
                            .map(timestamp -> statsDeletion.deleteAt(targetServiceUrl, timestamp))
                            .orElse(Try.of(() -> new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .map(ResponseEntity::getBody);
    }
}
