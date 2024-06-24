package ru.gbp.bot.commands.impl.middleServiceClient;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.commands.Command;
import ru.gbp.bot.commands.CommandType;
import ru.gbp.bot.service.CreateSendMessageService;
import ru.gbp.bot.service.UserService;

@Component
@AllArgsConstructor
public class CreateAccountCommandImpl implements MiddleServiceCommand {
    private final CreateSendMessageService createSendMessageService;
    private final UserService userService;
    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        String result = "";
        try{
            userService.createAccount(update.getMessage().getChatId(),update.getMessage().getChat().getUserName());
            result = "Счет успешно создан";
        }catch (HttpClientErrorException exception){
            result = handleException(exception);
        }
        return createSendMessageService.config(update.getMessage().getChatId(), result);
    }

    @Override
    public CommandType getType() {
        return CommandType.CREATE_ACCOUNT;
    }

    @Override
    public String handleException(HttpClientErrorException exception) {
        if (exception.getStatusCode().value()==409){
            return  "Счет уже был зарегистрирован";
        }
        else if(exception.getStatusCode().value()==404){
            return  "Для создания счета нужна регистрация";
        }
        return "Ошибка";
    }
}
