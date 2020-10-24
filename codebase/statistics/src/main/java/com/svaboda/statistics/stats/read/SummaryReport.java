package com.svaboda.statistics.stats.read;

import com.svaboda.storage.stats.domain.StatsFindings;
import com.svaboda.storage.stats.domain.StatsPeriod;
import com.svaboda.utils.UnifiedDateTime;
import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
class SummaryReport {

    private static final String TITLE = "Summary Report";

    String title = TITLE;
    String timeInfo = UnifiedDateTime.INFO;
    String generatedAt = UnifiedDateTime.now().toString();
    long totalCalls;
    long uniqueUsersCount;
    Map<String,Long> totalCommandCalls;
    Map<StatsPeriod.Period,PeriodReport> periodReports;

    static SummaryReport forPeriods(List<StatsPeriod.Period> periods, StatsFindings statsFindings) {
        final var periodReports = new HashMap<StatsPeriod.Period,PeriodReport>(periods.size());
        periods.forEach(period -> {
            final var filteredStatsFinding = statsFindings.filterBy(period);
            periodReports.putIfAbsent(period, PeriodReport.from(filteredStatsFinding));
        });
        return new SummaryReport(
                statsFindings.commandTotalSummary().totalCalls(),
                statsFindings.totalUsersCount(),
                statsFindings.commandTotalSummary().commandCalls(),
                periodReports
        );
    }

    @Value
    private static class PeriodReport {
        String title;
        long totalCalls;
        long uniqueUsersCount;
        Map<String,Long> commandCalls;

        private static PeriodReport from(StatsFindings statsFindings) {
            final var cmdCalls = new HashMap<String,Long>();
            statsFindings.commandCalls().forEach(element ->
                element.commandCalls().forEach((cc,callCount) -> {
                   cmdCalls.computeIfPresent(cc, (__,value) -> callCount.longValue() + value);
                    cmdCalls.putIfAbsent(cc, callCount.longValue());
                })
            );
            return new PeriodReport(
                    statsFindings.forPeriod().name(),
                    statsFindings.commandCalls().stream()
                        .map(commandCalls -> commandCalls
                                .commandCalls().values().stream()
                                    .reduce(0, Integer::sum))
                        .reduce(0, Integer::sum),
                    statsFindings.uniqueChats().size(),
                    cmdCalls
            );
        }
    }

}
