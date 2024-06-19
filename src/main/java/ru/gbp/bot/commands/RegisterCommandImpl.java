package ru.gbp.bot.commands;

import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.service.CreateSendMessageService;
import ru.gbp.bot.service.UserService;

@Component
@AllArgsConstructor
public class RegisterCommandImpl implements Command {
    private final CreateSendMessageService createSendMessageService;
    private final UserService userService;
    @Override
    public SendMessage processCommand(Update update) throws TelegramApiException {
        String result;
        try{
            userService.registerUser(update.getMessage().getChatId(),update.getMessage().getChat().getUserName());
            result = "Вы успешно зарегистрировались";
        }catch (FeignException exception){
            result = "Вы уже зарегистрированы";
        }
        return createSendMessageService.config(update.getMessage().getChatId(), result);
    }

    @Override
    public CommandType getType() {
        return CommandType.REGISTER;
    }
}
