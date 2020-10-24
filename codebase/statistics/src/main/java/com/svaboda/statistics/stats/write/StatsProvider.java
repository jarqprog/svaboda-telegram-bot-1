package com.svaboda.statistics.stats.write;

import com.svaboda.storage.stats.domain.StatsDto;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@AllArgsConstructor
class StatsProvider {

    private final WebClient webClient;

    Try<ResponseEntity<List<StatsDto>>> statsFrom(String url) {
        return Try.of(() -> webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(StatsDto.class)
                .block()
        );
    }

}
