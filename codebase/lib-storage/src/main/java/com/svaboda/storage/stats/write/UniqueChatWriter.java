package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.domain.UniqueChat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Set;

import static com.svaboda.storage.stats.StatsDb.Fields.CHAT_ID;
import static com.svaboda.storage.stats.StatsDb.Fields.DATE_HOUR;

@Slf4j
@RequiredArgsConstructor
class UniqueChatWriter {

    private final MongoTemplate mongoTemplate;

    long write(Set<UniqueChat> uniqueChats) {
        final var updated = uniqueChats.stream()
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
                UniqueChat.Entity.class
        ).getMatchedCount();
    }

}
