package ru.gbp.bot.commands.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.commands.Command;
import ru.gbp.bot.service.CreateSendMessageService;

@AllArgsConstructor
@Component
public class PingCommandImpl implements Command {
    private final CreateSendMessageService createSendMessageService;
    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        return createSendMessageService.config(update.getMessage().getChatId(), "/pong");
    }

    @Override
    public String getType() {
        return "/ping";
    }
}
