package com.svaboda.statistics.stats.read;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.svaboda.storage.stats.read.StatsReadRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.svaboda.utils.TimePeriod.Period.*;

@Slf4j
@RequiredArgsConstructor
class StatsReportProvider {

    private final StatsReadRepository statsReadRepository;

    Try<String> monthly() {
        final var periodsToBeIncluded = List.of(CURRENT_MONTH, TODAY, PREVIOUS_HOUR, CURRENT_HOUR);
        return statsReadRepository.fromLastMonth()
                .map(findings -> SummaryReport.generateFor(periodsToBeIncluded, findings))
                .map(SummaryReportPrinter::print)
                .peek(__ -> log.info("Monthly stats published"));
    }

    interface SummaryReportPrinter {

        ObjectMapper MAPPER = new ObjectMapper()
                .setVisibility(FIELD, ANY)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setSerializationInclusion(NON_ABSENT)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        static String print(SummaryReport summaryReport) {
            try {
                return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(summaryReport);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error occurred on printing statistics", e);
            }
        }

    }

}
