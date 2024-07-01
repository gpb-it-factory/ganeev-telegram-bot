package ru.gbp.bot.commands.impl.middleServiceClient;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.gbp.bot.dto.AccountsListResponseV2;

import ru.gbp.bot.service.CreateSendMessageService;
import ru.gbp.bot.service.UserService;

@Component
@AllArgsConstructor
@Slf4j
public class CreateAccountCommandImpl implements MiddleServiceCommand {
    private final CreateSendMessageService createSendMessageService;
    private final UserService userService;
    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        String result = "";
        log.debug("Создание пользователя");
        try{
            String[] message = update.getMessage().getText().split(" ");
            String accountName = "";
            if (message.length>1){
                accountName = message[1];
            }
            ResponseEntity<AccountsListResponseV2> response  = userService.createAccount(update.getMessage().getChatId(),accountName);
            result = "Счет успешно создан";
            if (response.hasBody()){
                log.debug("Счет №{} успешно зарегистрировался",response.getBody().getAccountId());
            }
        }catch (HttpClientErrorException exception){
            result = handleException(exception);
        }
        return createSendMessageService.config(update.getMessage().getChatId(), result);
    }

    @Override
    public String getType() {
        return "/create_account";
    }

    @Override
    public String handleException(HttpClientErrorException exception) {
        if (exception.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)){
            log.warn("Попытка создать существующий счет");
            return  "Счет уже был зарегистрирован";
        }
        else if(exception.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)){
            log.warn("Пользователь не зарегистрирован");
            return  "Для создания счета нужна регистрация";
        }
        log.error("Middle service unexpected error");
        return "Сервис временно недоступен";
    }
}
