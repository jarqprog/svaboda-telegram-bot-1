package com.svaboda.statistics.stats.write;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@AllArgsConstructor
class StatsDeletion {

    private final WebClient webClient;

    void deleteAt(String url, LocalDateTime at) {
        webClient.delete()
            .uri(deletionUrl(url, at))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toBodilessEntity()
            .block();
    }

    private String deletionUrl(String url, LocalDateTime at) {
        return url + "?at=" + at;
    }

}
