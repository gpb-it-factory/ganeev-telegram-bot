package ru.gbp.bot.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum CommandType {

    PING("/ping"),
    UNKNOWN_MES("UNKNOWN_MES"),
    HELP("/help"),
    REGISTER("/register"),
    CREATE_ACCOUNT("/create_account");


    private final String name;

    public static CommandType createCommandType(String messageTypename){
        for (CommandType commandType: CommandType.values()){
            if (commandType.getName().equals(messageTypename.toLowerCase())){
                return commandType;
            }
        }
        return CommandType.UNKNOWN_MES;
    }
}
