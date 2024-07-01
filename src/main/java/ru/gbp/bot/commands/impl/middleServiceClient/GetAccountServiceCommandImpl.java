package ru.gbp.bot.commands.impl.middleServiceClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class GetAccountServiceCommandImpl implements MiddleServiceCommand {
    private final CreateSendMessageService createSendMessageService;
    private final UserService userService;

    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        String result = "";
        try {
            log.debug("Получение счета");
            AccountsListResponseV2 account = userService.getAccount(update.getMessage().getChatId()).getBody();
            result = "Cчет: " + account.getAccountId() + "\n" +
                    "Название: " + account.getAccountName() + "\n" +
                    "Баланс: " + account.getAmount();
        } catch (HttpClientErrorException exception) {
            result = handleException(exception);
        }
        return createSendMessageService.config(update.getMessage().getChatId(), result);
    }

    @Override
    public String getType() {
        return "/get_account";
    }

    @Override
    public String handleException(HttpClientErrorException exception) {
        if (exception.getStatusCode().value() == 404) {
            log.warn("Запрос информации о несозданном счета");
            return "Счет еще не создан";
        }
        log.error("Middle service unexpected error");
        return "Сервер временно недоступен";
    }
}
