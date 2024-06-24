package ru.gbp.bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.commands.Command;
import ru.gbp.bot.commands.CommandContainer;
import ru.gbp.bot.commands.CommandType;
import ru.gbp.bot.commands.impl.HelpCommandImpl;
import ru.gbp.bot.commands.impl.PingCommandImpl;
import ru.gbp.bot.commands.impl.UnknownCommandImpl;
import ru.gbp.bot.commands.impl.middleServiceClient.CreateAccountCommandImpl;
import ru.gbp.bot.commands.impl.middleServiceClient.RegisterCommandImpl;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ProcessMessageServiceTest {


    ProcessMessageService processMessageService;

    CommandContainer commandContainer;
    @Mock
    UserService userService;
    CreateSendMessageService createSendMessageService = new CreateSendMessageService();
    Update getUpdateWithMessage(String text){
        Update update = new Update();
        Message message = new Message();
        message.setMessageId(1);
        Chat chat = new Chat(1L, "Чат");
        chat.setUserName("User1");
        message.setChat(chat);
        message.setText(text);
        update.setMessage(message);
        return update;
    }

    @BeforeEach
    void fillCommandContainer(){
        List<Command> commands = new ArrayList<>(List.of(
                new HelpCommandImpl(createSendMessageService),
                new PingCommandImpl(createSendMessageService),
                new UnknownCommandImpl(createSendMessageService),
                new RegisterCommandImpl(createSendMessageService,userService),
                new CreateAccountCommandImpl(createSendMessageService,userService)
        ));
        commandContainer = new CommandContainer(commands);
        processMessageService = new ProcessMessageService(commandContainer);
    }

    @Test
    void executeMessagePingTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage(CommandType.PING, getUpdateWithMessage("/ping"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("/pong", sendMessage.getText());
    }
    @Test
    void executeMessageHelpTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage(CommandType.HELP, getUpdateWithMessage("/help"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Основные команды:\n" + "1) /register\n" + "2) /create_account\n", sendMessage.getText());
    }
    @Test
    void executeDontExistMessageTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage(CommandType.UNKNOWN_MES, getUpdateWithMessage("Нету такой команды"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Неизвестная команда", sendMessage.getText());
    }

    @Test
    void successExecuteRegisterMessageTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage(CommandType.REGISTER, getUpdateWithMessage("/register"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Вы успешно зарегистрировались", sendMessage.getText());
    }

    @Test
    void failedExecuteRegisterMessageTest() throws TelegramApiException {
        Mockito.when(userService.registerUser(1,"User1")).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(409)));
        SendMessage sendMessage = processMessageService.executeMessage(CommandType.REGISTER, getUpdateWithMessage("/register"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Вы уже зарегистрированы", sendMessage.getText());
    }

    @Test
    void successExecuteCreateAccountMessageTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage(CommandType.CREATE_ACCOUNT, getUpdateWithMessage("/create_account"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Счет успешно создан", sendMessage.getText());
    }

    @Test
    void failedExecuteCreateAccountMessageTest() throws TelegramApiException {
        Mockito.when(userService.createAccount(1,"User1")).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(409)));
        SendMessage sendMessage = processMessageService.executeMessage(CommandType.CREATE_ACCOUNT, getUpdateWithMessage("/create_account"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Счет уже был зарегистрирован", sendMessage.getText());
    }
    @Test
    void failedExecuteCreateAccountMessageForNotExistUserTest() throws TelegramApiException {
        Mockito.when(userService.createAccount(1,"User1")).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(404)));
        SendMessage sendMessage = processMessageService.executeMessage(CommandType.CREATE_ACCOUNT, getUpdateWithMessage("/create_account"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Для создания счета нужна регистрация", sendMessage.getText());
    }

}
