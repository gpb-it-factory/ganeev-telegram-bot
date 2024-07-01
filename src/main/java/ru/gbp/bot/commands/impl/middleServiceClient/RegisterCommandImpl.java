package ru.gbp.bot.commands.impl.middleServiceClient;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class RegisterCommandImpl implements MiddleServiceCommand {
    private final CreateSendMessageService createSendMessageService;
    private final UserService userService;
    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        String result;
        try{
            log.debug("Регистрация пользователя");
            var response = userService.registerUser(update.getMessage().getChatId(),update.getMessage().getChat().getUserName());
            result = "Вы успешно зарегистрировались";
            if(response.hasBody()){
                log.debug("Пользователь {} был зарегистрирован ",response.getBody().getUserId());
            }
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
            log.warn("Попытка повторной регистрации");
            return "Вы уже зарегистрированы";
        }
        log.error("Middle service unexpected error");
        return "Сервер временно недоступен";
    }
}
