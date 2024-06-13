package ru.gbp.bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gbp.bot.commands.CommandContainer;
import ru.gbp.bot.commands.CommandType;
import ru.gbp.bot.commands.PingCommandImpl;

@ExtendWith(SpringExtension.class)
class ProcessMessageServiceTest {
    @InjectMocks
    ProcessMessageService processMessageService;
    @Mock
    CommandContainer commandContainer;
    @Mock
    CreateSendMessageService createSendMessageService;

    @Test
    void executeMessageTest() throws TelegramApiException {
        Update update = new Update();
        Message message = new Message();
        message.setMessageId(1);
        message.setChat(new Chat(1L, "Чат"));
        message.setText("/ping");
        update.setMessage(message);
        Mockito.when(commandContainer.getCommandExecutor(CommandType.PING)).thenReturn(new PingCommandImpl(createSendMessageService));
        Mockito.when(createSendMessageService.config(Mockito.anyLong(), Mockito.anyString())).thenAnswer(invocation -> {
            invocation.getArgument(0);
            return new SendMessage(
                    invocation.getArgument(0).toString(),
                    invocation.getArgument(1)
            );
        });

        SendMessage sendMessage = processMessageService.executeMessage(CommandType.PING, update);

        Assertions.assertEquals("1", sendMessage.getChatId());
        Assertions.assertEquals("/pong", sendMessage.getText());

    }

}
