package com.svaboda.bot.stats;

import com.svaboda.utils.Endpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.svaboda.bot.stats.StatisticsProperties.DATE_TIME_FORMAT;

@RestController
@RequiredArgsConstructor
class StatisticsResource {

    private final StatisticsProvider statisticsProvider;
    private final StatisticsDeletion statisticsDeletion;

    @GetMapping(Endpoints.STATS_INTERNAL)
    List<Statistics> statistics() {
        return statisticsProvider.provide().get();
    }

    @DeleteMapping(Endpoints.STATS_INTERNAL)
    void delete(@RequestParam(value = "at") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime at) {
        statisticsDeletion.deleteAt(at).get();
    }
}
