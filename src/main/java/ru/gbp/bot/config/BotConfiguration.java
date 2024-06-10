package ru.gbp.bot.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.gbp.bot.TelegramBot;

@Configuration
public class BotConfiguration {
    @Bean
    TelegramBotsApi telegramBotsApi(TelegramBot telegramBot){
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        };
        return telegramBotsApi;
    }
}
