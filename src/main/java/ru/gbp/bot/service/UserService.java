package ru.gbp.bot.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gbp.bot.dto.AccountsListResponseV2;
import ru.gbp.bot.dto.CreateAccountRequestV2;

import ru.gbp.bot.dto.CreateUserRequestV2;
import ru.gbp.bot.dto.UserResponseV2;
import ru.gbp.bot.exceptions.UserAlreadyExistsException;
import ru.gbp.bot.feign.MiddleServiceClient;

@Service
@AllArgsConstructor
public class UserService {
    private final MiddleServiceClient middleServiceClient;


    public ResponseEntity<AccountsListResponseV2> getAccount(long userId) {
        return middleServiceClient.getAccount(userId);
    }
    public ResponseEntity<UserResponseV2> registerUser(long userId, String userName){
        return middleServiceClient.createUser(new CreateUserRequestV2(userId, userName));
    }
    public ResponseEntity<AccountsListResponseV2> createAccount(long userId, String accountName){
        return middleServiceClient.createAccount(userId, new CreateAccountRequestV2(accountName));
    }
}
