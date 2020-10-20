package com.svaboda.statistics.stats;

import com.svaboda.storage.stats.Statistic;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@AllArgsConstructor
class StatsProvider {

    private final WebClient webClient;

    Try<ResponseEntity<List<Statistic>>> statsFrom(String url) {
        return Try.of(() -> webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Statistic.class)
                .block()
        );
    }
}
