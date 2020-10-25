package com.svaboda.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static com.svaboda.utils.TimePeriod.Formatter.*;
import static com.svaboda.utils.ArgsValidation.notEmpty;
import static com.svaboda.utils.TimePeriod.ServiceDateTime.now;

public interface TimePeriod {

    interface ServiceDateTime {

        String INFO = "Time based on UTC/GMT+0 (https://greenwichmeantime.com/time-zone/gmt-plus-0/)";
        String GMT = "GMT";
        ZoneId defaultZoneId = ZoneId.of(GMT);

        static LocalDateTime now() {
            return LocalDateTime.now(defaultZoneId);
        }

    }

    interface Formatter {
        DateTimeFormatter OF_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter OF_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter OF_HOUR = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");
    }

    static String hourFormat(LocalDateTime localDateTime) {
        return localDateTime.format(OF_HOUR);
    }

    static String hourFormatNow() {
        return now().format(OF_HOUR);
    }

    static String hourFormatNowMinusSec(long secondToSubtract) {
        return now().minusSeconds(secondToSubtract).format(OF_HOUR);
    }

    enum Period {
        CURRENT_MONTH(OF_MONTH, 0),
        TODAY(OF_DAY, 0),
        PREVIOUS_HOUR(OF_HOUR, 1),
        CURRENT_HOUR(OF_HOUR, 0);

        private final DateTimeFormatter dateTimeFormatter;
        private final long hourOffsetToSubtract;

        Period(DateTimeFormatter dateTimeFormatter, long hourOffsetToSubtract) {
            this.dateTimeFormatter = dateTimeFormatter;
            this.hourOffsetToSubtract = hourOffsetToSubtract;
        }

        public boolean matches(String hourlyDate) {
            return notEmpty(hourlyDate).startsWith(searchFilter());
        }

        public Pattern searchRegex() {
            return Pattern.compile(searchFilter());
        }

        private String searchFilter() {
            return now().minus(Duration.ofHours(hourOffsetToSubtract)).format(dateTimeFormatter);
        }

    }

}