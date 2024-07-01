package ru.gbp.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccountRequestV2 {
    private String accountName;
}
