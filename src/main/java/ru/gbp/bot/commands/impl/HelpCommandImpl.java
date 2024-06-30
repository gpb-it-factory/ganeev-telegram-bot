package ru.gbp.bot.commands.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.commands.Command;
import ru.gbp.bot.commands.CommandContainer;
import ru.gbp.bot.service.CreateSendMessageService;

@AllArgsConstructor
@Component
public class HelpCommandImpl implements Command {
    private final CreateSendMessageService createSendMessageService;
    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        StringBuilder resultMessage;
        resultMessage = new StringBuilder("Основные команды: \n");
        var commands = CommandContainer.getCommands();
        for(String command:commands){
            resultMessage.append(command).append("\n");
        }
        return createSendMessageService.config(update.getMessage().getChatId(), resultMessage.toString());
    }

    @Override
    public String getType() {
        return "/help";
    }
}
