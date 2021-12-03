package me.draimgoose.draimlogin.telegram;

import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.spigot.listeners.PlayerListener;
import me.draimgoose.draimlogin.spigot.listeners.LoginListener;
import me.draimgoose.draimlogin.telegram.callbackmanager.CallbackHandler;
import me.draimgoose.draimlogin.telegram.callbackmanager.TextCommandHandler;
import org.bukkit.Bukkit;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {
    private final DraimLogin plugin;
    private final CallbackHandler callbackHandler = CallbackHandler.getInstance();
    private final TextCommandHandler textCommandHandler = TextCommandHandler.getInstance();

    public TelegramBot(DraimLogin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new LoginListener(plugin, this), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(plugin, this), plugin);
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callbackHandler.run(update);
        } else textCommandHandler.run(update);
    }

    @Override
    public String getBotUsername() {
        return plugin.getConfig().getString("bot.name");
    }

    @Override
    public String getBotToken() {
        return plugin.getConfig().getString("bot.token");
    }


}
