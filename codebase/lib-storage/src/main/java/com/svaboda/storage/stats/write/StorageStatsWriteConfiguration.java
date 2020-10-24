package com.svaboda.storage.stats.write;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
class StorageStatsWriteConfiguration {

    @Bean
    StatsWriteRepository statsWriteRepository(MongoTemplate mongo) {
        return new MongoWriter(new CommandCountWriter(mongo), new CommandCallsWriter(mongo), new UniqueChatWriter(mongo));
    }

}
