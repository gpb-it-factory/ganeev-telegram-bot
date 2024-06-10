package ru.gbp.bot.commands;


import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class CommandsArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(CommandType.PING,"/ping"),
                org.junit.jupiter.params.provider.Arguments.of(CommandType.HELP,"/help"),
                org.junit.jupiter.params.provider.Arguments.of(CommandType.UNKNOWN_MES,"unknown_mes"),
                org.junit.jupiter.params.provider.Arguments.of(CommandType.UNKNOWN_MES,"INVALID")
        );
    }
}



