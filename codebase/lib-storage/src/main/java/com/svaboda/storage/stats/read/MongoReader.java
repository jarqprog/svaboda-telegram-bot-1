package com.svaboda.storage.stats.read;

import com.svaboda.storage.stats.domain.CommandCalls;
import com.svaboda.storage.stats.domain.UniqueChat;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.svaboda.storage.stats.StatsDb.DATE_MONTH_FORMAT;
import static com.svaboda.storage.stats.StatsDb.Fields.DATE_HOUR;

@RequiredArgsConstructor
class MongoReader implements StatsReadRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Try<StatsFindings> fromLastMonth() {
        final var fromLastMonthQuery = fromLastMonthQuery();
        return Try.of(() -> new StatsFindings(
                commandCallsBy(fromLastMonthQuery),
                uniqueChatsBy(fromLastMonthQuery))
        );
    }

    private List<CommandCalls> commandCallsBy(Query query) {
        return mongoTemplate.find(
                query,
                CommandCalls.Entity.class
        ).parallelStream()
                .map(CommandCalls::from)
                .collect(Collectors.toList());
    }

    private List<UniqueChat> uniqueChatsBy(Query query) {
        return mongoTemplate.find(
                query,
                UniqueChat.Entity.class
        ).parallelStream()
                .map(UniqueChat::from)
                .collect(Collectors.toList());
    }

    private Query fromLastMonthQuery() {
        final var yearMonth = LocalDateTime.now().format(DATE_MONTH_FORMAT);
        return new Query(Criteria.where(DATE_HOUR).regex(Pattern.compile(yearMonth)));
    }

}
