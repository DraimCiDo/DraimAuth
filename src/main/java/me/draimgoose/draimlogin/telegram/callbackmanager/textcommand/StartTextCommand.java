package me.draimgoose.draimlogin.telegram.callbackmanager.textcommand;

import me.draimgoose.draimlogin.telegram.TelegramBot;
import me.draimgoose.draimlogin.telegram.callbackmanager.AbstractUpdate;
import me.draimgoose.draimlogin.util.LangConstants;
import me.draimgoose.draimlogin.util.MessageFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartTextCommand extends AbstractUpdate {
    @Override
    public void onUpdateCall(TelegramBot bot, Update update, String[] args) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        try {
            bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_START_MESSAGE.getString()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
