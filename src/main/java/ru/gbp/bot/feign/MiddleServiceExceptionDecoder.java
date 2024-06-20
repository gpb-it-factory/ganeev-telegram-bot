package ru.gbp.bot.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.gbp.bot.exceptions.UserAlreadyExistsException;

public class MiddleServiceExceptionDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 409) {
            return new UserAlreadyExistsException();
        }
        return errorDecoder.decode(methodKey,response);
    }
}
