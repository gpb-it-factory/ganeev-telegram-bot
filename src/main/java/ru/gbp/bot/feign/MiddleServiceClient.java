package ru.gbp.bot.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.gbp.bot.config.FeignConfig;
import ru.gbp.bot.dto.AccountsListResponseV2;
import ru.gbp.bot.dto.CreateAccountRequestV2;

import ru.gbp.bot.dto.CreateUserRequestV2;
import ru.gbp.bot.dto.UserResponseV2;

@FeignClient(name = "${feign.name}",url = "${feign.url}",configuration = FeignConfig.class)
public interface MiddleServiceClient {

    @GetMapping("/users/{id}/accounts")
    ResponseEntity<AccountsListResponseV2> getAccount(@PathVariable("id") long id);
    @PostMapping("users")
    ResponseEntity<UserResponseV2> createUser(@RequestBody CreateUserRequestV2 createUserRequest);
    @PostMapping("users/{id}/accounts")
    ResponseEntity<AccountsListResponseV2> createAccount(@PathVariable("id") long id, @RequestBody(required = false) CreateAccountRequestV2 createAccountRequestV2);

}
