package me.draimgoose.draimlogin.spigot.listeners;

import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.data.PlayerData;
import me.draimgoose.draimlogin.telegram.TelegramBot;
import me.draimgoose.draimlogin.util.*;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class LoginListener implements Listener {
    private final DraimLogin plugin;
    private final TelegramBot bot;
    private final PlayerData playerData = PlayerData.getInstance();

    public LoginListener(DraimLogin plugin, TelegramBot bot) {
        this.plugin = plugin;
        this.bot = bot;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (playerData.getPlayerCache().containsKey(player.getUniqueId())) {
            TelegramPlayer telegramPlayer = playerData.getPlayerCache().get(player.getUniqueId());
            if (telegramPlayer.isLocked()) {
                player.kickPlayer(LangConstants.INGAME_KICK_ACCOUNT_LOCKED.getFormattedString());
                return;
            }
            if (plugin.isLoginSessionEnabled() && telegramPlayer.getPlayerIp().equalsIgnoreCase(player.getAddress().getHostString())) {
                player.sendMessage(LangConstants.INGAME_LOGINSESSION_LOGGED.getFormattedString());
                return;
            }
            playerData.getPlayerInLogin().put(player.getUniqueId(), telegramPlayer);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (plugin.isBungeeEnabled())
                    Util.sendPluginMessage(player, PluginMessageAction.ADD);
            }, 3);
            try {
                bot.execute(MessageFactory.loginRequest(telegramPlayer.getPlayerUUID(), telegramPlayer.getChatID(), player.getName(), player.getAddress().getHostString()));
                player.sendMessage(LangConstants.INGAME_WAIT_FOR_LOGIN_CONFIRM.getFormattedString());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
        plugin.getSql().getTelegramPlayer(player.getUniqueId().toString()).whenComplete((telegramPlayer, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }).thenAccept(telegramPlayer -> {
            if (telegramPlayer == null) {
                if (plugin.getConfig().getBoolean("2FA.enabled")) return;
                playerData.getPlayerWaitingForChatid().add(player.getUniqueId());
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (plugin.isBungeeEnabled())
                        Util.sendPluginMessage(player, PluginMessageAction.ADD);
                }, 3);

                player.sendMessage(LangConstants.INGAME_ADD_CHATID.getFormattedString().replaceAll("%bot_tag%", plugin.getConfig().getString("bot.name")));
            } else {
                if (telegramPlayer.isLocked()) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> player.kickPlayer(LangConstants.INGAME_KICK_ACCOUNT_LOCKED.getFormattedString()), 1);
                    return;
                }
                if (plugin.isLoginSessionEnabled()) {
                    telegramPlayer.setPlayerIp(player.getAddress().getHostString());
                }
                playerData.getPlayerCache().put(player.getUniqueId(), telegramPlayer);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (plugin.isBungeeEnabled())
                        Util.sendPluginMessage(player, PluginMessageAction.ADD);
                }, 3);
                playerData.getPlayerInLogin().put(player.getUniqueId(), telegramPlayer);
                try {
                    bot.execute(MessageFactory.loginRequest(telegramPlayer.getPlayerUUID(), telegramPlayer.getChatID(), player.getName(), player.getAddress().getHostString()));
                    player.sendMessage(LangConstants.INGAME_WAIT_FOR_LOGIN_CONFIRM.getFormattedString());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerData.getPlayerInLogin().remove(player.getUniqueId());
        playerData.getPlayerWaitingForChatid().remove(player.getUniqueId());
    }
}

