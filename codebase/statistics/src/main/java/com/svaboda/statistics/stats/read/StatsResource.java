package com.svaboda.statistics.stats.read;

import com.svaboda.utils.Endpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class StatsResource {

    private final StatsReportProvider statsReportProvider;

    @GetMapping(value = Endpoints.STATS_MONTH, produces = "text/plain")
    String monthlyReport() {
        return statsReportProvider.monthly()
                .get();
    }
}
