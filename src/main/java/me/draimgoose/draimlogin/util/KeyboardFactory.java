package me.draimgoose.draimlogin.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class KeyboardFactory {
    public static ReplyKeyboard addConfirmButtons(String playerUUID, String chatID) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                ButtonFactory.createButton(LangConstants.TG_CONFIRM_BUTTON_TEXT.getString(), "/addconfirm " + playerUUID + " " + chatID),
                ButtonFactory.createButton(LangConstants.TG_ABORT_BUTTON_TEXT.getString(), "/addabort " + playerUUID + " " + chatID)
        ));

        inlineKeyboard.setKeyboard(buttons);
        return inlineKeyboard;
    }

    public static ReplyKeyboard unlockButton(String playerUUID, String chatID) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                ButtonFactory.createButton(LangConstants.TG_UNLOCK_BUTTON_TEXT.getString(), "/unlock " + playerUUID + " " + chatID)
        ));

        inlineKeyboard.setKeyboard(buttons);
        return inlineKeyboard;
    }

    public static ReplyKeyboard loginRequestButtons(String playerUUID, String chatID) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                ButtonFactory.createButton(LangConstants.TG_CONFIRM_BUTTON_TEXT.getString(), "/loginconfirm " + playerUUID + " " + chatID),
                ButtonFactory.createButton(LangConstants.TG_ABORT_BUTTON_TEXT.getString(), "/abort " + playerUUID + " " + chatID)
        ));
        buttons.add(Collections.singletonList(
                ButtonFactory.createButton(LangConstants.TG_LOCK_BUTTON_TEXT.getString(), "/lock " + playerUUID + " " + chatID)
        ));

        inlineKeyboard.setKeyboard(buttons);
        return inlineKeyboard;
    }

}

