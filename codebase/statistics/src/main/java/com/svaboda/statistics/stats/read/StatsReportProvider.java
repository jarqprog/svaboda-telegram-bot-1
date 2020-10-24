package com.svaboda.statistics.stats.read;

import com.svaboda.storage.stats.read.StatsReadRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StatsReportProvider {

    private final StatsReadRepository statsReadRepository;

    String monthly() {
        System.out.println(statsReadRepository.fromLastMonth());
        return "monthly";
    }

}
