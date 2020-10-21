package com.svaboda.statistics.stats;

import com.svaboda.storage.failureinfo.FailureInfoRepository;
import com.svaboda.storage.stats.write.StatsWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({StatsProperties.class})
class StatsConfig {

    @Bean
    TaskScheduler taskScheduler(StatsProperties statsProperties,
                                StatsOperation statsOperation,
                                FailureInfoRepository failureInfoRepository) {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(statsProperties.servicesUrls().size());
        scheduler.initialize();
        schedule(scheduler, statsProperties, statsOperation, failureInfoRepository);
        return scheduler;
    }

    @Bean
    StatsOperation statsOperation(StatsWriteRepository statsWriteRepository, WebClient botClient) {
        return new StatsTransactionalOperation(
                statsWriteRepository,
                new StatsProvider(botClient),
                new StatsDeletion(botClient)
        );
    }

    private void schedule(ThreadPoolTaskScheduler scheduler,
                          StatsProperties statsProperties,
                          StatsOperation statsOperation,
                          FailureInfoRepository failureInfoRepository) {
        statsProperties.servicesUrls().forEach(targetServiceUrl -> {
                    log.info("Scheduling stats process for {}", targetServiceUrl);
                    final var stats = statsProcess(targetServiceUrl, statsOperation, failureInfoRepository);
                    scheduler.scheduleWithFixedDelay(stats::process, statsProperties.intervalSec());
                    log.info("stats process for {} scheduled", targetServiceUrl);
                }
        );
    }

    private StatsProcess statsProcess(String targetServiceUrl,
                                      StatsOperation statsOperation,
                                      FailureInfoRepository failureInfoRepository) {
        return new StatsProcess(targetServiceUrl, statsOperation, failureInfoRepository);
    }
}
