package ru.gbp.bot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.commands.CommandContainer;
import ru.gbp.bot.commands.CommandType;
import ru.gbp.bot.config.BotConfigProperties;
import ru.gbp.bot.service.ProcessMessageService;

import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final ProcessMessageService processMessageService;

    public TelegramBot(BotConfigProperties properties, ProcessMessageService processMessageService) {
        super(properties.getToken());
        this.properties = properties;
        this.processMessageService = processMessageService;
    }

    private final BotConfigProperties properties;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message = update.getMessage().getText();

            List<String> messageInfo = List.of(message.split(" "));

            CommandType commandType = CommandType.createCommandType(messageInfo.get(0));

            try {
                SendMessage sendMessage = processMessageService.executeMessage(commandType,update);
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }
}
