package me.draimgoose.draimlogin.telegram.callbackmanager.callbackcommand;

import me.draimgoose.draimlogin.telegram.TelegramBot;
import me.draimgoose.draimlogin.telegram.callbackmanager.AbstractUpdate;
import me.draimgoose.draimlogin.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;

public class LockCallbackQuery extends AbstractUpdate {
    @Override
    public void onUpdateCall(TelegramBot bot, Update update, String[] args) {
        String playerUUID = args[1];
        UUID uuid = UUID.fromString(playerUUID);
        Player player = Bukkit.getPlayer(uuid);
        String chatId = args[2];
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        try {
            bot.execute(deleteMessage);
            plugin.getSql().setLockPlayer(playerUUID, true);
            if (playerData.getPlayerCache().containsKey(UUID.fromString(playerUUID))){
                playerData.getPlayerCache().get(UUID.fromString(playerUUID)).setLocked(true);
            }
            if (player != null) {
                if (playerData.getPlayerInLogin().containsKey(uuid)){
                    playerData.getPlayerInLogin().remove(uuid);
                    Util.sendPluginMessage(player, PluginMessageAction.REMOVE);
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> player.kickPlayer(LangConstants.INGAME_KICK_ACCOUNT_LOCKED.getFormattedString()),1);
            }
            bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_LOCKED_MESSAGE.getString(), KeyboardFactory.unlockButton(playerUUID, chatId)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

}
