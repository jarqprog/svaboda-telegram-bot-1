package com.svaboda.storage.stats.domain;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.svaboda.utils.ArgsValidation.notEmpty;
import static com.svaboda.utils.ArgsValidation.positive;

@Value
public class CommandCount {

    String command;
    long count;

    public CommandCount(String command, long count) {
        this.command = notEmpty(command);
        this.count = positive(count);
    }

    public static CommandCount from(Entity entity) {
        return new CommandCount(entity.command, entity.count);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Document("commandCount")
    public static class Entity {
        @Id
        private ObjectId id;
        private String command;
        private long count;
    }
}
