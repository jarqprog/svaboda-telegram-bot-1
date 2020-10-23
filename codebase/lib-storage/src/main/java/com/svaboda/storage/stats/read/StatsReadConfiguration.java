package com.svaboda.storage.stats.read;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
class StatsReadConfiguration {

    @Bean
    StatsReadRepository statsReadRepository(MongoTemplate mongoTemplate) {
        return new MongoReader(mongoTemplate);
    }

}
