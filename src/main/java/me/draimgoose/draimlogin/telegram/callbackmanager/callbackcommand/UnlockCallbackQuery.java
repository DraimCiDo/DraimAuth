package me.draimgoose.draimlogin.telegram.callbackmanager.callbackcommand;

import me.draimgoose.draimlogin.telegram.TelegramBot;
import me.draimgoose.draimlogin.telegram.callbackmanager.AbstractUpdate;
import me.draimgoose.draimlogin.util.LangConstants;
import me.draimgoose.draimlogin.util.MessageFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;

public class UnlockCallbackQuery extends AbstractUpdate {
    @Override
    public void onUpdateCall(TelegramBot bot, Update update, String[] args) {
        String playerUUID = args[1];
        String chatId = args[2];
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        try {
            bot.execute(deleteMessage);
            plugin.getSql().setLockPlayer(playerUUID, false);
            if (playerData.getPlayerCache().containsKey(UUID.fromString(playerUUID))){
                playerData.getPlayerCache().get(UUID.fromString(playerUUID)).setLocked(false);
            }
            bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_UNLOCKED_MESSAGE.getString()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}

