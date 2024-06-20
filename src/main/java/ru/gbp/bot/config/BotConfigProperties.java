package ru.gbp.bot.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
@Data
public class BotConfigProperties {

    private final ApplicationArguments applicationArguments;

    @Value("${telegram-bot.name}")
    private String name;

    private String token;

    public BotConfigProperties(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @PostConstruct
    void tokenInit(){
        token = System.getenv("TELEGRAM_BOT_TOKEN");
    }
}
