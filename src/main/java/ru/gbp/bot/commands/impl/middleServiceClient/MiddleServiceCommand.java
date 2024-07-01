package ru.gbp.bot.commands.impl.middleServiceClient;

import org.apache.http.HttpException;
import org.springframework.web.client.HttpClientErrorException;
import ru.gbp.bot.commands.Command;

public interface MiddleServiceCommand extends Command {
    public String handleException(HttpClientErrorException exception);
}
