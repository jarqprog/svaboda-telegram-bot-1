package com.svaboda.storage.stats.read;

import com.svaboda.storage.stats.domain.*;
import com.svaboda.utils.TimePeriod;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.stream.Collectors;

import static com.svaboda.storage.stats.StatsDb.Fields.DATE_HOUR;

@RequiredArgsConstructor
class MongoReader implements StatsReadRepository {

    private static final Query FIND_ALL = new Query();
    private final MongoTemplate mongoTemplate;

    @Override
    public Try<StatsFindings> fromLastMonth() {
        final var period = TimePeriod.Period.CURRENT_MONTH;
        return findWith(statsPeriodQuery(period), period);
    }

    private Try<StatsFindings> findWith(Query query, TimePeriod.Period period) {
        return Try.of(() -> StatsFindings.create(
                period,
                totalUniqueUsers(),
                totalCommandCount(),
                commandCallsBy(query),
                uniqueChatsBy(query))
        );
    }

    private long totalUniqueUsers() {
        return mongoTemplate.count(FIND_ALL, UniqueChat.Entity.class);
    }

    private CommandTotalSummary totalCommandCount() {
        return CommandTotalSummary.from(
                mongoTemplate.findAll(CommandCount.Entity.class).stream()
                        .map(CommandCount::from)
                        .collect(Collectors.toList())
        );
    }

    private List<CommandCalls> commandCallsBy(Query query) {
        return mongoTemplate.find(query, CommandCalls.Entity.class).stream()
                .map(CommandCalls::from)
                .collect(Collectors.toList());
    }

    private List<UniqueChat> uniqueChatsBy(Query query) {
        return mongoTemplate.find(query, UniqueChat.Entity.class).stream()
                .map(UniqueChat::from)
                .collect(Collectors.toList());
    }

    private Query statsPeriodQuery(TimePeriod.Period period) {
        return new Query(Criteria.where(DATE_HOUR).regex(period.searchRegex()));
    }

}
