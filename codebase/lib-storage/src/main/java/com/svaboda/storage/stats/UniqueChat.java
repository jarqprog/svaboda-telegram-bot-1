package com.svaboda.storage.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class UniqueChat {

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