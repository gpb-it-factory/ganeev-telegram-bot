package ru.gbp.bot.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gbp.bot.feign.MiddleServiceExceptionDecoder;

@Configuration
public class FeignConfig {
    @Bean
    ErrorDecoder exceptionDecoder() {
        return new MiddleServiceExceptionDecoder();
    }
}
