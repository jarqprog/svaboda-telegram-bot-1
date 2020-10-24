package com.svaboda.statistics.stats.read;

import com.svaboda.utils.Endpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class StatsResource {

    private final StatsReportProvider statsReportProvider;

    @GetMapping(Endpoints.STATS_MONTH)
    String monthlyReport() {
        return statsReportProvider.monthly();
    }
}
