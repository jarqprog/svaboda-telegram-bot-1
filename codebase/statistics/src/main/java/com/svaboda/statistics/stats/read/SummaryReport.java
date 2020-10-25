package com.svaboda.statistics.stats.read;

import com.svaboda.storage.stats.domain.StatsFindings;
import com.svaboda.utils.TimePeriod;
import lombok.Value;

import java.util.*;

@Value
class SummaryReport {

    private static final String TITLE = "Summary Report";

    String title = TITLE;
    String timeInfo = TimePeriod.ServiceDateTime.INFO;
    String generatedAt = TimePeriod.ServiceDateTime.now().toString();
    long totalCalls;
    long uniqueUsersCount;
    Map<String, Long> totalCommandCalls;
    SortedMap<TimePeriod.Period, PeriodReport> periodReports;

    static SummaryReport generateFor(List<TimePeriod.Period> periods, StatsFindings statsFindings) {
        final var periodReports = new TreeMap<TimePeriod.Period, PeriodReport>();
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
    static class PeriodReport {
        String title;
        long totalCalls;
        long uniqueUsersCount;
        Map<String, Long> commandCalls;

        private static PeriodReport from(StatsFindings statsFindings) {
            final var cmdCalls = new HashMap<String, Long>();
            statsFindings.commandCalls().forEach(element ->
                    element.commandCalls().forEach((cc, callCount) -> {
                        cmdCalls.putIfAbsent(cc, 0L);
                        cmdCalls.computeIfPresent(cc, (__, value) -> callCount.longValue() + value);
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
