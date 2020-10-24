package com.svaboda.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public interface UnifiedDateTime {

    String INFO = "Time based on UTC/GMT+0 (https://greenwichmeantime.com/time-zone/gmt-plus-0/)";
    String GMT = "GMT";
    ZoneId defaultZoneId = ZoneId.of(GMT);

    static LocalDateTime now() {
        return LocalDateTime.now(defaultZoneId);
    }

}
