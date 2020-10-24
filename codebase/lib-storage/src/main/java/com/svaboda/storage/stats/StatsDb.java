package com.svaboda.storage.stats;

import java.time.format.DateTimeFormatter;

public interface StatsDb {

    DateTimeFormatter DATE_HOUR_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");
    DateTimeFormatter DATE_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

    interface Fields {
        String CHAT_ID = "chatId";
        String DATE_HOUR = "dateHour";
    }

}
