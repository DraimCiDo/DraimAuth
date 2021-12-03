package me.draimgoose.draimlogin.telegram.callbackmanager.callbackcommand;

import me.draimgoose.draimlogin.telegram.TelegramBot;
import me.draimgoose.draimlogin.telegram.callbackmanager.AbstractUpdate;
import me.draimgoose.draimlogin.util.LangConstants;
import me.draimgoose.draimlogin.util.MessageFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;

public class AbortCallbackQuery extends AbstractUpdate {
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
            if (player == null) {
                bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_PLAYER_OFFLINE.getString()));
                return;
            }
            if (playerData.getPlayerWaitingForChatid().contains(uuid)) {
                playerData.getPlayerWaitingForChatid().remove(uuid);
                player.sendMessage(LangConstants.INGAME_OPERATION_ABORTED.getFormattedString());
                bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_ABORTED_MESSAGE.getString()));
                return;
            }
            if (playerData.getPlayerInLogin().containsKey(uuid)) {
                playerData.getPlayerInLogin().remove(uuid);
                player.sendMessage(LangConstants.INGAME_OPERATION_ABORTED.getFormattedString());
                Bukkit.getScheduler().runTaskLater(plugin, () -> player.kickPlayer(LangConstants.INGAME_KICK_LOG_AGAIN.getFormattedString()), 1);
                bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_ABORTED_MESSAGE.getString()));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}

