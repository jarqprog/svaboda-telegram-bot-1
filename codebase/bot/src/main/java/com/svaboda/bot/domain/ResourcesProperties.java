package com.svaboda.bot.domain;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.svaboda.utils.ArgsValidation.notEmpty;
import static com.svaboda.utils.ArgsValidation.positive;

@ConfigurationProperties(prefix = "env.telegram.texts")
@ConstructorBinding
@Value
public class ResourcesProperties {

    String path;
    String fileExtension;
    int maxResourceSize;
    String topicEnrichmentLine;
    String goToArticleLine;
    String header;

    public ResourcesProperties(
            String path,
            String fileExtension,
            Integer maxResourceSize,
            String topicEnrichmentLine,
            String goToArticleLine,
            String header) {
        this.path = notEmpty(path);
        this.fileExtension = notEmpty(fileExtension);
        this.maxResourceSize = positive(maxResourceSize);
        this.topicEnrichmentLine = "\n\n" + notEmpty(topicEnrichmentLine) + "\n\n";
        this.goToArticleLine = "\n\n" + notEmpty(goToArticleLine) + " ";
        this.header = "\n" + notEmpty(header) + "\n\n";
    }

}