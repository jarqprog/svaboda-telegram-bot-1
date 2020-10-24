package com.svaboda.storage.stats.write;

import com.svaboda.storage.stats.CommandCalls;
import com.svaboda.storage.stats.TotalCommandCalls;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static com.svaboda.storage.stats.StatsDb.Fields.DATE_HOUR;

@Slf4j
@RequiredArgsConstructor
class TotalCommandCallsWriter {

//    private static final Query FIND_ALL = new Query();
//    private final MongoTemplate mongoTemplate;
//
//    Try<Void> write(CommandCalls commandCalls) {
//        return upsert(commandCalls);
//    }
//
//    private Try<Void> upsert(CommandCalls commandCalls) {
//        return Try.run(() -> {
//                    final var result = mongoTemplate.findAllAndRemove(
//                            FIND_ALL, TotalCommandCalls.class, TOTAL_COMMAND_CALLS
//                    )
//                            .map
//                            .map(commandCalls::merge)
//                            .or(() -> Optional.of(commandCalls))
//                            .map(mongoTemplate::insert)
//                            .get();
//                    log.info("Saved CommandCalls with DateHour={}", saved.dateHour());
//                }
//        );
//    }
//
//    private Query findByDateHourQuery(CommandCalls commandCalls) {
//        return new Query(Criteria.where(DATE_HOUR).is(commandCalls.dateHour()));
//    }

}
