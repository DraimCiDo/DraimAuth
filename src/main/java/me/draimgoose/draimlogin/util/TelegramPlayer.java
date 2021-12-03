package me.draimgoose.draimlogin.util;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
public class TelegramPlayer {
    private final String playerUUID;
    @Setter
    private String chatID;
    @Setter
    private boolean locked;
    private final Date registrationDate;
    @Setter
    private String playerIp;

    public TelegramPlayer(String playerUUID, String chatID, boolean locked, Date registrationDate, String playerIp) {
        this(playerUUID, chatID, locked, registrationDate);
        this.playerIp = playerIp;
    }

    public TelegramPlayer(String playerUUID, String chatID, boolean locked, Date registrationDate) {
        this.playerUUID = playerUUID;
        this.chatID = chatID;
        this.locked = locked;
        this.registrationDate = registrationDate;
    }
}

