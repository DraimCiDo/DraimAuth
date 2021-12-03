package me.draimgoose.draimlogin.telegram.callbackmanager;

import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.data.PlayerData;
import me.draimgoose.draimlogin.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractUpdate {
    protected final PlayerData playerData = PlayerData.getInstance();
    protected final DraimLogin plugin = DraimLogin.getInstance();

    public abstract void onUpdateCall(TelegramBot bot, Update update, String[] args);
}
