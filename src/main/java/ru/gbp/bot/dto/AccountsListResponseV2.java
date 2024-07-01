package ru.gbp.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AccountsListResponseV2 {
    private String accountId;

    private String accountName;

    private double amount;
}
