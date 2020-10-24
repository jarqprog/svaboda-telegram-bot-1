package com.svaboda.storage.stats.domain;

import com.svaboda.utils.UnifiedDateTime;

import java.time.format.DateTimeFormatter;

public interface StatsPeriod {

    enum Period {
        CURRENT_MONTH(DateTimeFormatter.ofPattern("yyyy-MM")),
        TODAY(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        LAST_HOUR(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH"));

        private final DateTimeFormatter dateTimeFormatter;

        Period(DateTimeFormatter dateTimeFormatter) {
            this.dateTimeFormatter = dateTimeFormatter;
        }

        public DateTimeFormatter formatter() {
            return dateTimeFormatter;
        }

        public String searchFilter() {
            return UnifiedDateTime.now().minusHours(1).format(formatter());
        }

    }

}