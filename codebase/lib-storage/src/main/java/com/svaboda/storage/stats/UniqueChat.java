package com.svaboda.storage.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.svaboda.utils.ArgsValidation.notEmpty;
import static com.svaboda.utils.ArgsValidation.positive;

@Value
public class UniqueChat {

    long chatId;
    String dateHour;

    public UniqueChat(long chatId, String dateHour) {
        this.chatId = positive(chatId);
        this.dateHour = notEmpty(dateHour);
    }

    public static UniqueChat from(UniqueChat.Entity entity) {
        return new UniqueChat(entity.chatId, entity.dateHour);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Document("uniqueChat")
    public static class Entity {
        @Id
        private long chatId;
        private String dateHour;
    }
}