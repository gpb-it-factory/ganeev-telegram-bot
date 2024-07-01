package ru.gbp.bot.commands.impl.middleServiceClient;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.service.CreateSendMessageService;
import ru.gbp.bot.service.UserService;

@Component
@AllArgsConstructor
public class RegisterCommandImpl implements MiddleServiceCommand {
    private final CreateSendMessageService createSendMessageService;
    private final UserService userService;
    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        String result;
        try{
            userService.registerUser(update.getMessage().getChatId(),update.getMessage().getChat().getUserName());
            result = "Вы успешно зарегистрировались";
        }catch (HttpClientErrorException exception){
            result = handleException(exception);
        }
        return createSendMessageService.config(update.getMessage().getChatId(), result);
    }

    @Override
    public String getType() {
        return "/register";
    }

    @Override
    public String handleException(HttpClientErrorException exception) {
        if (exception.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)){
            return "Вы уже зарегистрированы";
        }
        return "Сервер временно недоступен";
    }
}
