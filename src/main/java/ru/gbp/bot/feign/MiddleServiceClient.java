package ru.gbp.bot.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.gbp.bot.dto.CreateUserRequestV2;
import ru.gbp.bot.dto.UserResponseV2;

@FeignClient(name = "${feign.name}",url = "${feign.url}")
public interface MiddleServiceClient {
    @PostMapping("users")
    void createUser(@RequestBody CreateUserRequestV2 createUserRequest);

}
