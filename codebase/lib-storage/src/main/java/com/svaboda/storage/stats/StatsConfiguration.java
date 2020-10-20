package com.svaboda.storage.stats;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
class StatsConfiguration {

    @Bean
    StatsRepository statsRepository(MongoTemplate mongoTemplate) {
        return new MongoDatabase(mongoTemplate);
    }

}
