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
                .map(chatId -> new UniqueChat(chatId, generatedAt))
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
        final static String REGISTERED_AT = "registeredAt";
        @Id
        long chatId;

        String registeredAt;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UniqueChat)) return false;

            UniqueChat that = (UniqueChat) o;

            return chatId == that.chatId;
        }

        @Override
        public int hashCode() {
            return (int) (chatId ^ (chatId >>> 32));
        }
    }
}
