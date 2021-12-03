package me.draimgoose.draimlogin.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class MessageFactory {

    public static SendMessage simpleMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    public static SendMessage simpleMessage(String chatId, String text, ReplyKeyboard buttons) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(buttons);
        return sendMessage;
    }

    public static SendMessage chatidMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(LangConstants.TG_CHATID_MESSAGE.getString().replaceAll("%chatId%", chatId));
        return sendMessage;
    }

    public static SendMessage loginRequest(String playerUUID, String chatId, String playerName, String ipAddress) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(LangConstants.TG_LOGIN_MESSAGE.getString()
                .replaceAll("%player_name%", playerName)
                .replaceAll("%player_ip%", ipAddress)
        );
        sendMessage.setReplyMarkup(KeyboardFactory.loginRequestButtons(playerUUID, chatId));
        return sendMessage;
    }

}
