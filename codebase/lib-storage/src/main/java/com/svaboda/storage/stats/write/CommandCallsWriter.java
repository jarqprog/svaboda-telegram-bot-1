package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.CommandCalls;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

import static com.svaboda.storage.stats.StatsDb.Documents.COMMAND_CALLS;
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
                            .map(commandCalls::merge)
                            .or(() -> Optional.of(commandCalls))
                            .map(mongoTemplate::insert)
                            .get();
                    log.info("Saved CommandCalls with DateHour={}", saved.dateHour());
                }
        );
    }

    private Optional<CommandCalls> find(CommandCalls commandCalls) {
        return Optional.ofNullable(
                mongoTemplate.findAndRemove(findByDateHourQuery(commandCalls), CommandCalls.class, COMMAND_CALLS)
        );
    }

    private Query findByDateHourQuery(CommandCalls commandCalls) {
        return new Query(Criteria.where(DATE_HOUR).is(commandCalls.dateHour()));
    }

}
