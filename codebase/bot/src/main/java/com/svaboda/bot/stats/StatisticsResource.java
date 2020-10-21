package com.svaboda.bot.stats;

import com.svaboda.utils.Endpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
class StatisticsResource {

    private final StatisticsProvider statisticsProvider;
    private final StatisticDeletion statisticDeletion;

    @GetMapping(Endpoints.STATS)
    List<HourlyStatistics> statistics() {
        return statisticsProvider.provide().get();
    }

    @DeleteMapping(Endpoints.STATS)
    void delete() {
        statisticDeletion.delete().get();
    }
}
