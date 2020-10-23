package com.svaboda.storage.stats.read;

import com.svaboda.storage.stats.CommandCalls;
import com.svaboda.storage.stats.UniqueChat;
import lombok.Value;

import java.util.List;

@Value
public class StatsFindings {
    List<CommandCalls> commandCalls;
    List<UniqueChat> uniqueChats;
}
