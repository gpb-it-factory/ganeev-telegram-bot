package ru.gbp.bot.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import ru.gbp.bot.exceptions.UserAlreadyExistsException;

public class MiddleServiceExceptionDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 409, 404 -> new HttpClientErrorException(HttpStatusCode.valueOf(response.status()));
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}
