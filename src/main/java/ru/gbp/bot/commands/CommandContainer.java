package ru.gbp.bot.commands;

import jakarta.validation.groups.Default;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gbp.bot.commands.impl.UnknownCommandImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommandContainer {

    private final Map<String,Command> commands;
    private final UnknownCommandImpl unknownCommand;

    private static Set<String> availableCommands;

    public CommandContainer(List<Command> commandList,UnknownCommandImpl unknownCommand) {
        this.commands = commandList.stream().collect(Collectors.toMap(Command::getType, Function.identity()));
        this.unknownCommand = unknownCommand;
        availableCommands = commands.keySet();
    }
    public Command getCommandExecutor(String type){
        return commands.getOrDefault(type,unknownCommand);
    }
    public static Set<String> getCommands(){
        return availableCommands;
    }

}
