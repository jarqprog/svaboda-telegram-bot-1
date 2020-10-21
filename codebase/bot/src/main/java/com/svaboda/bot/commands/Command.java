package com.svaboda.bot.commands;

import lombok.Value;

import java.util.Optional;

import static com.svaboda.utils.ArgsValidation.notEmpty;

@Value
public class Command {
    public final static Command TOPICS_INSTANCE = new Command("spisok_tem", "spisok_tem", null);

    String name;
    String resourceId;
    String externalLink;

    Command(String name, String resourceId, String externalLink) {
        this.name = notEmpty(name);
        this.resourceId = notEmpty(resourceId);
        this.externalLink = externalLink;
    }

    public boolean isTopicsCommand() {
        return this.equals(TOPICS_INSTANCE);
    }

    public Optional<String> externalLink() {
        return Optional.ofNullable(externalLink);
    }

}