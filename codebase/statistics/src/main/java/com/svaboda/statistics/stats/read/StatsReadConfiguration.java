package com.svaboda.statistics.stats.read;

import com.svaboda.storage.stats.read.StatsReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class StatsReadConfiguration {

    @Bean
    StatsReportProvider statsReportProvider(StatsReadRepository statsReadRepository) {
        return new StatsReportProvider(statsReadRepository);
    }

}
