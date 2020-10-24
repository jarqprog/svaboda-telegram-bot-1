package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.domain.CommandCalls;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

import static com.svaboda.storage.stats.StatsDb.Fields.DATE_HOUR;

@Slf4j
@RequiredArgsConstructor
class CommandCallsWriter {

    private final MongoTemplate mongoTemplate;

    Try<Void> write(CommandCalls commandCalls) {
        return upsert(commandCalls);
    }

    private Try<Void> upsert(CommandCalls commandCalls) {
        return Try.run(() -> {
                    final var saved = find(commandCalls)
                            .map(CommandCalls::from)
                            .map(commandCalls::merge)
                            .or(() -> Optional.of(commandCalls))
                            .map(CommandCalls::entity)
                            .map(mongoTemplate::insert)
                            .get();
                    log.info("Saved CommandCalls with DateHour={}", saved.dateHour());
                }
        );
    }

    private Optional<CommandCalls.Entity> find(CommandCalls commandCalls) {
        return Optional.ofNullable(
                mongoTemplate.findAndRemove(
                        findByDateHourQuery(commandCalls),
                        CommandCalls.Entity.class
                )
        );
    }

    private Query findByDateHourQuery(CommandCalls commandCalls) {
        return new Query(Criteria.where(DATE_HOUR).is(commandCalls.dateHour()));
    }

}
