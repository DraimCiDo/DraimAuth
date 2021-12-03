package me.draimgoose.draimlogin.telegram.callbackmanager.textcommand;

import me.draimgoose.draimlogin.telegram.TelegramBot;
import me.draimgoose.draimlogin.telegram.callbackmanager.AbstractUpdate;
import me.draimgoose.draimlogin.util.LangConstants;
import me.draimgoose.draimlogin.util.MessageFactory;
import me.draimgoose.draimlogin.util.PluginMessageAction;
import me.draimgoose.draimlogin.util.Util;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;

public class LockTextCommand extends AbstractUpdate {
    @Override
    public void onUpdateCall(TelegramBot bot, Update update, String[] args) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        try {
            if (args.length != 2) {
                bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_LOCK_USAGE.getString()));
                return;
            }
            if (!NumberUtils.isDigits(args[1])) {
                bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_INVALID_VALUE.getString()));
                return;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        int accountId = Integer.parseInt(args[1]);

        plugin.getSql().getTelegramPlayer(chatId, accountId).whenComplete((telegramPlayer, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(telegramPlayer -> {
            try {
                if (telegramPlayer == null) {
                    bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_ACCOUNTID_NOT_LINKED.getString()));
                } else {
                    plugin.getSql().setLockPlayer(accountId, true);
                    if (playerData.getPlayerCache().containsKey(UUID.fromString(telegramPlayer.getPlayerUUID()))) {
                        playerData.getPlayerCache().get(UUID.fromString(telegramPlayer.getPlayerUUID())).setLocked(true);
                    }
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Player player = Bukkit.getPlayer(UUID.fromString(telegramPlayer.getPlayerUUID()));
                        if (player != null) {
                            if (playerData.getPlayerInLogin().containsKey(player.getUniqueId())) {
                                playerData.getPlayerInLogin().remove(player.getUniqueId());
                                if (plugin.isBungeeEnabled()) {
                                    Util.sendPluginMessage(player, PluginMessageAction.REMOVE);
                                }
                            }
                            player.kickPlayer(LangConstants.INGAME_KICK_ACCOUNT_LOCKED.getFormattedString());
                        }
                    }, 1);
                    bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_LOCKED_MESSAGE_BY_COMMAND.getString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
