package com.svaboda.storage.stats.write;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Set;

import static com.svaboda.storage.stats.write.StatisticWrite.DATE_HOUR;
import static com.svaboda.storage.stats.write.UniqueChat.CHAT_ID;

@Slf4j
@RequiredArgsConstructor
class UniqueChatWriter {

    private final MongoTemplate mongoTemplate;

    long write(Set<UniqueChat> uniqueChats) {
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
