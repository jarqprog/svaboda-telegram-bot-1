package com.svaboda.storage.stats;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

import static com.google.common.collect.Sets.difference;
import static com.svaboda.storage.stats.Statistic.UniqueChat.CHAT_ID;
import static com.svaboda.storage.stats.Statistic.UniqueChat.REGISTERED_AT;

@Slf4j
@RequiredArgsConstructor
class MongoDatabase implements StatsRepository {

    private final static int MAX_PROCESSABLE_UPSERT_SIZE = 50;
    private final static Query ALL = new Query();
    private final MongoTemplate mongoTemplate;

    @Override
    public Try<Void> upsertAll(List<Statistic> statistics) {
        return Try.run(() -> statistics.forEach(this::upsert))
                .onFailure(failure -> log.error("Error occurred on saving Statistics", failure));
    }
//
//    @Override
//    public Try<Void> saveAll(List<Statistics> statistics) {
//        return Try.run(() -> statistics.forEach(it -> mongoTemplate.save(Stats.from(it))))
//                .peek(__ -> log.info("Stats saved"))
//                .onFailure(failure -> log.error("Error occurred on saving Stats", failure));
//    }
//
//    @Override
//    public Try<List<Stats>> loadAllByTimestamps(List<String> timestamps) {
//        final var query = new Query()
//                .addCriteria(Criteria.where(Stats.TIMESTAMP).in(timestamps));
//        return Try.of(() -> mongoTemplate.find(query, Stats.class));
//    }


    private void upsert(Statistic statistic) {
        mongoTemplate.save(statistic.asStats());//todo add proper implementation
        final var uniqueChats = statistic.asUniqueChats();
        final var processed = processUniqueChats(uniqueChats);
        log.info("Stats saved with observed new unique chats={}", processed);
    }

    private long processUniqueChats(Set<Statistic.UniqueChat> uniqueChats) {
        return shouldProceedWithInsert(uniqueChats) ? withInsert(uniqueChats) : withUpsert(uniqueChats);
    }

    private boolean shouldProceedWithInsert(Set<Statistic.UniqueChat> uniqueChats) {
        return uniqueChats.size() > MAX_PROCESSABLE_UPSERT_SIZE;
    }

    private long withInsert(Set<Statistic.UniqueChat> uniqueChats) {
        Query onlyChatIds = new Query();
        onlyChatIds.fields().include(CHAT_ID);
        log.info("Processing with INSERT");
        if (!uniqueChats.isEmpty()) {
            final var alreadySavedUniqueChats = new HashSet<>(mongoTemplate.find(onlyChatIds, Statistic.UniqueChat.class));
            final var diff = difference(uniqueChats, alreadySavedUniqueChats);
            if (!diff.isEmpty()) {
                return mongoTemplate.insertAll(diff).size();
            }
        }
        return 0;
    }

    private long withUpsert(Set<Statistic.UniqueChat> uniqueChats) {
        log.info("Processing with UPSERT");
        final var updated = uniqueChats.parallelStream()
                .map(this::upsertUniqueChat)
                .reduce(0L, Long::sum);
        return uniqueChats.size() - updated;
    }

    private long upsertUniqueChat(Statistic.UniqueChat uniqueChat) {
        return mongoTemplate.upsert(
                new Query(Criteria.where(CHAT_ID).is(uniqueChat.chatId)),
                new Update()
                        .setOnInsert(CHAT_ID, uniqueChat.chatId)
                        .setOnInsert(REGISTERED_AT, uniqueChat.registeredAt),
                Statistic.UniqueChat.class
        ).getMatchedCount();
    }

    private Set<Statistic.UniqueChat> generateOfSize(int size) {
        final var result = new ArrayList<Statistic.UniqueChat>(size);
        for (int i = 0; i< size; i++) {
            result.add(new Statistic.UniqueChat(90569900L+i, String.valueOf(i)));
        }
        return new HashSet<>(result);
    }
}
