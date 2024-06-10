package ru.gbp.bot.commands;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommandContainer {

    private final Map<CommandType,Command> commands;

    public CommandContainer(List<Command> commandList) {
        this.commands = commandList.stream().collect(Collectors.toMap(Command::getType, Function.identity()));
    }
    public Command getCommandExecutor(CommandType type){
        return commands.get(type);
    }
}
