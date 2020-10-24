package com.svaboda.storage.stats.read;

import com.svaboda.storage.stats.domain.CommandCalls;
import com.svaboda.storage.stats.domain.UniqueChat;
import lombok.Value;

import java.util.List;

@Value
public class StatsFindings {
    List<CommandCalls> commandCalls;
    List<UniqueChat> uniqueChats;
}
