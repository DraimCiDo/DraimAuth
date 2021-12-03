package me.draimgoose.draimlogin.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class TelegramPlayerInfo {
    private final int accountId;
    private final String playerName;
    @Setter
    private boolean locked;
}
