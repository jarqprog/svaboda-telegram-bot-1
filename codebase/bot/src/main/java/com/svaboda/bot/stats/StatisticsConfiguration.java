package com.svaboda.bot.stats;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({StatisticsProperties.class})
public class StatisticsConfiguration {

    @Bean(name = {"statisticsRegistration", "statisticsProvider", "statisticDeletion", "statisticsHandler"})
    public StatisticsHandler statisticsHandler(StatisticsProperties statisticsProperties) {
        return new CachedStatistics(statisticsProperties.aggregationMode());
    }
}
