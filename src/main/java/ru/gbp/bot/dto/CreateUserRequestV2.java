package ru.gbp.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequestV2 {
    private long userId;
    private String userName;
}
