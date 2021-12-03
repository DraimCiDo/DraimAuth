package me.draimgoose.draimlogin.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.draimgoose.draimlogin.util.TelegramPlayer;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlayerData {
    private final Map<UUID, TelegramPlayer> playerInLogin = new HashMap<>();
    private final Set<UUID> playerWaitingForChatid = new HashSet<>();
    private final Set<UUID> bungeePendingPlayers = new HashSet<>();
    private final Map<UUID, TelegramPlayer> playerCache = new HashMap<>();

    @Getter(lazy = true)
    private static final PlayerData instance = new PlayerData();
}
