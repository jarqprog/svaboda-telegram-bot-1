package com.svaboda.storage.stats.write;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

@RequiredArgsConstructor
class CommandCallsWriter {

    private final MongoTemplate mongoTemplate;

    Try<Void> write(CommandCalls commandCalls) {
        return Try.run(() -> mongoTemplate.save(commandCalls));
    }
}
