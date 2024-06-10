package ru.gbp.bot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gbp.bot.service.CreateSendMessageService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommandsTest {

    @Mock
    CreateSendMessageService createSendMessageService;


    @ParameterizedTest
    @ArgumentsSource(CommandsArgumentProvider.class)
    void createCommandTest(CommandType commandType, String commandTypeName) {
        Assertions.assertEquals(commandType, CommandType.createCommandType(commandTypeName));
    }

    @ParameterizedTest
    @EnumSource(CommandType.class)
    void getCommand(CommandType commandType) {
        Command commandPing = new PingCommandImpl(createSendMessageService);
        Command commandUnknown = new UnknownCommandImpl(createSendMessageService);
        Command commandHelp = new HelpCommandImpl(createSendMessageService);

        CommandContainer commandContainer = new CommandContainer(List.of(commandPing, commandUnknown, commandHelp));
        commandContainer.getCommandExecutor(commandType);
    }
}