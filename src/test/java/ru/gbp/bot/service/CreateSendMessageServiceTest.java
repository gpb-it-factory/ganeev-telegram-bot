package ru.gbp.bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class CreateSendMessageServiceTest {
    CreateSendMessageService createSendMessageService = new CreateSendMessageService();
    @Test
    void checkMessageCreated(){
        SendMessage sendMessage = createSendMessageService.config(1,"Сообщение");
        Assertions.assertEquals("1",sendMessage.getChatId());
        Assertions.assertEquals("Сообщение",sendMessage.getText());
    }
}








