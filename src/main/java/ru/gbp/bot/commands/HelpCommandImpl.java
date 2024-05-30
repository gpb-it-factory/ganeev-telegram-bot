package ru.gbp.bot.commands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.service.CreateSendMessageService;

@AllArgsConstructor
@Component
public class HelpCommandImpl implements Command {
    private final CreateSendMessageService createSendMessageService;
    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        return createSendMessageService.config(update.getMessage().getChatId(), "Сейчас бот отвечает только на /ping");
    }

    @Override
    public CommandType getType() {
        return CommandType.HELP;
    }
}
