package com.svaboda.statistics.stats.read;

import com.svaboda.storage.stats.domain.StatsFindings;
import com.svaboda.storage.stats.domain.StatsPeriod;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class StatsReportFactory {

    private static final String MONTHLY = "Monthly report";

    SummaryReport monthlyFrom(StatsFindings statsFindings) {
        final var monthlyReport = PeriodReport.monthly(statsFindings);
        return new SummaryReport(
                MONTHLY,
                LocalDateTime.now().toString(),
                statsFindings.commandTotalSummary().totalCalls(),
                statsFindings.totalUsersCount(),
                statsFindings.commandTotalSummary().commandCalls(),
                Map.of(statsFindings.period(), monthlyReport)
        );
    }

    @Value
    private static class SummaryReport {
        String title;
        String generatedAt;
        long totalCalls;
        long uniqueUsersCount;
        Map<String,Long> totalCommandCalls;
        Map<StatsPeriod.Period,PeriodReport> periodReports;
    }

    @Value
    private static class PeriodReport {
        String title;
        long totalCalls;
        long uniqueUsersCount;
        Map<String,Long> commandCalls;

        private static PeriodReport monthly(StatsFindings statsFindings) {
            final var cmdCalls = new HashMap<String,Long>();
            statsFindings.commandCalls().forEach(element ->
                element.commandCalls().forEach((cc,callCount) -> {
                   cmdCalls.putIfAbsent(cc, callCount.longValue());
                   cmdCalls.computeIfPresent(cc, (__,value) -> callCount + value);
                })
            );
            return new PeriodReport(
                    "LAST " + statsFindings.period().name(),
                    statsFindings.commandCalls().parallelStream()
                        .map(commandCalls -> commandCalls.commandCalls().values().stream()
                                    .reduce(0, Integer::sum))
                        .reduce(0, Integer::sum),
                    statsFindings.uniqueChats().size(),
                    cmdCalls
            );
        }
    }

}
