package com.svaboda.bot.stats;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.svaboda.utils.ArgsValidation.notEmpty;

@Value
@ConfigurationProperties(prefix = "env.statistics")
@ConstructorBinding
public class StatisticsProperties {
    static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";

    Duration aggregationMode;

    public StatisticsProperties(String aggregationMode) {
        this.aggregationMode = resolve(notEmpty(aggregationMode));
    }

    private Duration resolve(String mode) {
        return switch (AcceptableModes.valueOf(mode)) {
            case SECOND -> ChronoUnit.SECONDS.getDuration();
            case MINUTE -> ChronoUnit.MINUTES.getDuration();
            case HOUR -> ChronoUnit.HOURS.getDuration();
        };
    }

    private enum AcceptableModes {
        SECOND, MINUTE, HOUR
    }
}
