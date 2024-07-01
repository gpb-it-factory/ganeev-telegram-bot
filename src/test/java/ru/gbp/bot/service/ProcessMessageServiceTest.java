package ru.gbp.bot.service;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.commands.Command;
import ru.gbp.bot.commands.CommandContainer;


import ru.gbp.bot.commands.impl.HelpCommandImpl;
import ru.gbp.bot.commands.impl.PingCommandImpl;
import ru.gbp.bot.commands.impl.UnknownCommandImpl;
import ru.gbp.bot.commands.impl.middleServiceClient.CreateAccountCommandImpl;

import ru.gbp.bot.commands.impl.middleServiceClient.GetAccountServiceCommandImpl;
import ru.gbp.bot.commands.impl.middleServiceClient.RegisterCommandImpl;
import ru.gbp.bot.dto.AccountsListResponseV2;

import ru.gbp.bot.dto.UserResponseV2;



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
                new RegisterCommandImpl(createSendMessageService,userService),
                new CreateAccountCommandImpl(createSendMessageService,userService),
                new GetAccountServiceCommandImpl(createSendMessageService,userService)
        ));
        commandContainer =new CommandContainer(commands,new UnknownCommandImpl(createSendMessageService));
        processMessageService = new ProcessMessageService(commandContainer);
    }

    @Test
    void executeMessagePingTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage("/ping", getUpdateWithMessage("/ping"));


    @Test
    void executeMessagePingTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage("/ping", getUpdateWithMessage("/ping"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("/pong", sendMessage.getText());
    }
    @Test
    void executeMessageHelpTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage("/help", getUpdateWithMessage("/help"));

        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Основные команды: \n"+"/get_account\n"+ "/help\n"+ "/create_account\n" + "/ping\n" + "/register\n", sendMessage.getText());

    }
    @Test
    void executeDontExistMessageTest() throws TelegramApiException {
        SendMessage sendMessage = processMessageService.executeMessage("123asdf", getUpdateWithMessage("Нету такой команды"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Неизвестная команда", sendMessage.getText());
    }

    @Test
    void successExecuteRegisterMessageTest() throws TelegramApiException {
        Mockito.when(userService.registerUser(1,"User1")).thenReturn(ResponseEntity.ok(new UserResponseV2("111")));

        SendMessage sendMessage = processMessageService.executeMessage("/register", getUpdateWithMessage("/register"));

        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Вы успешно зарегистрировались", sendMessage.getText());
    }

    @Test
    void failedExecuteRegisterMessageTest() throws TelegramApiException {
        Mockito.when(userService.registerUser(1,"User1")).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(409)));

        SendMessage sendMessage = processMessageService.executeMessage("/register", getUpdateWithMessage("/register"));

        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Вы уже зарегистрированы", sendMessage.getText());
    }

    @Test
    void successExecuteCreateAccountMessageTest() throws TelegramApiException {
        Mockito.when(userService.createAccount(1,"User1")).thenReturn(ResponseEntity.ok(new AccountsListResponseV2("111","Акционный",5000d)));

        SendMessage sendMessage = processMessageService.executeMessage("/create_account", getUpdateWithMessage("/create_account"));

        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Счет успешно создан", sendMessage.getText());
    }

    @Test
    void failedExecuteCreateAccountMessageTest() throws TelegramApiException {
        Mockito.when(userService.createAccount(1,"User1")).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(409)));

        SendMessage sendMessage = processMessageService.executeMessage("/create_account", getUpdateWithMessage("/create_account"));

        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Счет уже был зарегистрирован", sendMessage.getText());
    }
    @Test
    void failedExecuteCreateAccountMessageForNotExistUserTest() throws TelegramApiException {
        Mockito.when(userService.createAccount(1,"User1")).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(404)));

        SendMessage sendMessage = processMessageService.executeMessage("/create_account", getUpdateWithMessage("/create_account"));

        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Для создания счета нужна регистрация", sendMessage.getText());
    }


    @Test
    void successExecuteGetAccountMessageTest() throws TelegramApiException {
        Mockito.when(userService.getAccount(1)).thenReturn(ResponseEntity.ok(new AccountsListResponseV2("111","Акционный",5000d)));
        SendMessage sendMessage = processMessageService.executeMessage("/get_account", getUpdateWithMessage("/get_account"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        String result =sendMessage.getText();
        Assertions.assertTrue(result.contains("111"));
        Assertions.assertTrue(result.contains("Акционный"));
        Assertions.assertTrue(result.contains(String.valueOf(5000d)));
    }

    @Test
    void failedExecuteGetAccountMessageTest() throws TelegramApiException {
        Mockito.when(userService.getAccount(1)).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(404)));
        SendMessage sendMessage = processMessageService.executeMessage("/get_account", getUpdateWithMessage("/get_account"));
        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("Счет еще не создан", sendMessage.getText());
    }


}
