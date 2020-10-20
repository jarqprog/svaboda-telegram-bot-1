package com.svaboda.storage.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.Id;

import java.util.*;
import java.util.stream.Collectors;

import static com.svaboda.storage.support.SerializationHelper.serialize;

@Data
public class Statistic {
    String generatedAt;
    Map<String,Integer> commandsCalls;
    Set<Long> uniqueChats;

    Stats asStats() {
        return new Stats(
                this.generatedAt(),
                serialize(Statistic.Payload.from(this))
        );
    }

    Set<UniqueChat> asUniqueChats() {
        return uniqueChats.stream()
                .map(UniqueChat::new)
                .collect(Collectors.toSet());
    }

    @Value
    static class Payload {
        String generatedAt;
        Map<String,Integer> commandsCalls;

        private static Payload from(Statistic statistic) {
            return new Payload(statistic.generatedAt, statistic.commandsCalls);
        }
    }

    @Data
    @AllArgsConstructor
    static class Stats {
        final static String TIMESTAMP = "timestamp";
        @Id
        private String timestamp;
        private String payload;
    }

    @Data
    @AllArgsConstructor
    static class UniqueChat {
        final static String CHAT_ID = "chatId";
        @Id
        long chatId;
    }
}
