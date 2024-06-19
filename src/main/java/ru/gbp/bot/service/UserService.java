package ru.gbp.bot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gbp.bot.dto.CreateUserRequestV2;
import ru.gbp.bot.feign.MiddleServiceClient;

@Service
@AllArgsConstructor
public class UserService {
    private final MiddleServiceClient middleServiceClient;
    public void registerUser(long userId, String userName){
        middleServiceClient.createUser(new CreateUserRequestV2(userId, userName));
    }
}
