package com.svaboda.storage.stats.write;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticWrite {
    final static String DATE_HOUR = "dateHour";

    private String dateHour;
    private Map<String,Object> commandsCalls;
    private Set<Long> uniqueChats;

    CommandCalls asCommandCalls() {
        return new CommandCalls(
                this.dateHour(),
                new Document(commandsCalls)
        );
    }

    Set<UniqueChat> asUniqueChats() {
        return uniqueChats.stream()
                .map(chatId -> new UniqueChat(chatId, dateHour))
                .collect(Collectors.toSet());
    }
}

@Data
@AllArgsConstructor
class CommandCalls {

    @Id
    private String dateHour;
    private Document commandCalls;

    CommandCalls merge(CommandCalls other) {
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

@Data
@AllArgsConstructor
class UniqueChat {
    final static String CHAT_ID = "chatId";

    @Id
    private long chatId;
    private String dateHour;

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
