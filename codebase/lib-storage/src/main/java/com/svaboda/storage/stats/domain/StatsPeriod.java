package com.svaboda.storage.stats.domain;

import java.time.format.DateTimeFormatter;

public interface StatsPeriod {

    enum Period {
        LAST_MONTH(DateTimeFormatter.ofPattern("yyyy-MM")),
        LAST_DAY(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        LAST_HOUR(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH"));

        private final DateTimeFormatter dateTimeFormatter;

        Period(DateTimeFormatter dateTimeFormatter) {
            this.dateTimeFormatter = dateTimeFormatter;
        }

        public DateTimeFormatter formatter() {
            return dateTimeFormatter;
        }

    }
}