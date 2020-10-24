package com.svaboda.storage.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalCommandCalls {

    @Id
    String command;
    long callsCount;

//    public static List<TotalCommandCalls> from(List<CommandCalls> commandCalls) {
//        return commandCalls.
//
//    }
}
