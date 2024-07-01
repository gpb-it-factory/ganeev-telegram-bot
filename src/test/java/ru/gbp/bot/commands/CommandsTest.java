package ru.gbp.bot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gbp.bot.commands.impl.HelpCommandImpl;
import ru.gbp.bot.commands.impl.PingCommandImpl;
import ru.gbp.bot.commands.impl.UnknownCommandImpl;
import ru.gbp.bot.service.CreateSendMessageService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommandsTest {

    @Mock
    CreateSendMessageService createSendMessageService;



    @ParameterizedTest
    @ValueSource(strings = {"/ping","/help","asdf[asd",""})
    void getCommand(String commandType) {
        Command commandPing = new PingCommandImpl(createSendMessageService);
        UnknownCommandImpl commandUnknown = new UnknownCommandImpl(createSendMessageService);
        Command commandHelp = new HelpCommandImpl(createSendMessageService);
        CommandContainer commandContainer = new CommandContainer(List.of(commandPing,commandHelp), commandUnknown);

        Assertions.assertNotNull(commandContainer.getCommandExecutor(commandType));
    }
}
