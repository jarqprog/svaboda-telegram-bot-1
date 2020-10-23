package com.svaboda.storage.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandCalls {

    @Id
    private String dateHour;
    private Document commandCalls;

    public CommandCalls merge(CommandCalls other) {
        validateDateHourTheSame(other);
        final var merged = new Document(commandCalls);
        other.commandCalls.forEach((key, value) -> {
            var callsCount = (int) value;
            merged.computeIfPresent(key, (__, cc) -> callsCount + (int) cc);
            merged.computeIfAbsent(key, __ -> value);
        });
        return new CommandCalls(dateHour, merged);
    }

    private void validateDateHourTheSame(CommandCalls other) {
        if (!dateHour.equals(other.dateHour)) throw new IllegalArgumentException("DateHour is not the same");
    }

}
