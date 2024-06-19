package ru.gbp.bot.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.commands.Command;
import ru.gbp.bot.commands.CommandContainer;
import ru.gbp.bot.commands.CommandType;

@AllArgsConstructor
@Slf4j
@Service
public class ProcessMessageService {

    private final CommandContainer commandContainer;

    public SendMessage executeMessage(CommandType commandType, Update update) throws TelegramApiException {
        Command command=commandContainer.getCommandExecutor(commandType);
        return command.processCommand(update);
    }
}
