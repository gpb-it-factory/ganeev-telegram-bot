package ru.gbp.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface Command {
    SendMessage processCommand(Update update) throws TelegramApiException;
    String getType();
}
