package com.svaboda.storage.stats.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

import static com.svaboda.utils.ArgsValidation.notEmpty;
import static com.svaboda.utils.ArgsValidation.notNull;

@Value
public class CommandCalls {

    String dateHour;
    Map<String, Integer> commandCalls;

    public CommandCalls(String dateHour, Map<String, Integer> commandCalls) {
        this.dateHour = notEmpty(dateHour);
        this.commandCalls = notNull(commandCalls);
    }

    public CommandCalls merge(CommandCalls other) {
        validateDateHourTheSame(other);
        final var merged = new HashMap<>(commandCalls);

        other.commandCalls.forEach((key, value) -> {
            merged.putIfAbsent(key, 0);
            merged.computeIfPresent(key, (__, cc) -> value + cc);
        });
        return new CommandCalls(dateHour, new HashMap<>(merged));
    }

    public static CommandCalls from(Entity entity) {
        final var commandCalls = new HashMap<String, Integer>();
        entity.commandCalls.forEach((key, value) -> commandCalls.putIfAbsent(key, (Integer) value));
        return new CommandCalls(entity.dateHour, commandCalls);
    }

    public Entity entity() {
        return new Entity(dateHour, new Document(new HashMap<>(commandCalls)));
    }

    private void validateDateHourTheSame(CommandCalls other) {
        if (!dateHour.equals(other.dateHour)) throw new IllegalArgumentException("DateHour is not the same");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @org.springframework.data.mongodb.core.mapping.Document("commandCalls")
    public static class Entity {
        @Id
        private String dateHour;
        private Document commandCalls;
    }

}
