package com.svaboda.statistics.stats;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@AllArgsConstructor
class StatsDeletion {

    private final WebClient webClient;

    Try<ResponseEntity<Void>> deleteAt(String url, LocalDateTime at) {
        return Try.of(() -> webClient.delete()
                .uri(deletionUrl(url, at))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .block()
        );
    }

    private String deletionUrl(String url, LocalDateTime at) {
        return url + "?at=" + at;
    }

}
