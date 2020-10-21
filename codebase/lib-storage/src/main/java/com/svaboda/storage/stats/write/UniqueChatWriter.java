package com.svaboda.storage.stats.write;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Sets.difference;
import static com.svaboda.storage.stats.write.StatisticWrite.DATE_HOUR;
import static com.svaboda.storage.stats.write.UniqueChat.CHAT_ID;

@Slf4j
@RequiredArgsConstructor
class UniqueChatWriter {

    private final static int MAX_PROCESSABLE_UPSERT_SIZE = 100;
    private final MongoTemplate mongoTemplate;

    long write(Set<UniqueChat> uniqueChats) {
        return shouldProceedWithInsert(uniqueChats) ? withInsert(uniqueChats) : withUpsert(uniqueChats);
    }

    private boolean shouldProceedWithInsert(Set<UniqueChat> uniqueChats) {
        return uniqueChats.size() > MAX_PROCESSABLE_UPSERT_SIZE;
    }

    private long withInsert(Set<UniqueChat> uniqueChats) {
        Query onlyChatIds = new Query();
        onlyChatIds.fields().include(CHAT_ID);
        log.info("Processing uniqueChats with INSERT");
        if (!uniqueChats.isEmpty()) {
            final var alreadySavedUniqueChats = new HashSet<>(mongoTemplate.find(onlyChatIds, UniqueChat.class));
            final var diff = difference(uniqueChats, alreadySavedUniqueChats);
            if (!diff.isEmpty()) {
                return mongoTemplate.insertAll(diff).size();
            }
        }
        return 0;
    }

    private long withUpsert(Set<UniqueChat> uniqueChats) {
        log.info("Processing uniqueChats with UPSERT");
        final var updated = uniqueChats.parallelStream()
                .map(this::upsertUniqueChat)
                .reduce(0L, Long::sum);
        return uniqueChats.size() - updated;
    }

    private long upsertUniqueChat(UniqueChat uniqueChat) {
        return mongoTemplate.upsert(
                new Query(Criteria.where(CHAT_ID).is(uniqueChat.chatId())),
                new Update()
                        .setOnInsert(CHAT_ID, uniqueChat.chatId())
                        .setOnInsert(DATE_HOUR, uniqueChat.dateHour()),
                UniqueChat.class
        ).getMatchedCount();
    }
}
