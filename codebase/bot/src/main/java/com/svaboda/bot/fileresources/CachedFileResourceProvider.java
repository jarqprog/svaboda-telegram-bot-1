package com.svaboda.bot.fileresources;

import com.svaboda.bot.commands.Command;
import com.svaboda.bot.domain.ResourceProvider;
import com.svaboda.bot.domain.ResourceTransformer;
import com.svaboda.bot.domain.TelegramResource;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.svaboda.utils.ArgsValidation.notNull;

class CachedFileResourceProvider implements ResourceProvider<String> {

    private static final Logger LOG = LoggerFactory.getLogger(CachedFileResourceProvider.class);

    private final Map<String, TelegramResource<String>> cache = new ConcurrentHashMap<>();
    private final TextFileResourceReader reader;
    private final ResourceTransformer<String> transformer;

    CachedFileResourceProvider(TextFileResourceReader reader, ResourceTransformer<String> transformer) {
        this.reader = notNull(reader);
        this.transformer = notNull(transformer);
    }

    @Override
    public Try<TelegramResource<String>> provideBy(Command command) {
        return Try.of(command::resourceId)
                .map(filename -> cache.computeIfAbsent(
                        filename,
                        __ -> resolveBy(command)
                        )
                );
    }

    private TelegramResource<String> resolveBy(Command command) {
        return reader.readFrom(command.resourceId())
                .flatMap(resource -> transformer.asContent(command, resource))
                .map(TelegramResource::new)
                .recoverWith(failure -> Try.failure(
                        new ReadingFileException("Unable to resolve resource " + command.resourceId(), failure)
                        )
                )
                .onFailure(failure -> LOG.error(failure.getMessage(), failure))
                .get();
    }

}
