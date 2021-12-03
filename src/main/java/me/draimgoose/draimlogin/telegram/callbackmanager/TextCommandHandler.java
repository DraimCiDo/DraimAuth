package me.draimgoose.draimlogin.telegram.callbackmanager;

import lombok.Getter;
import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.telegram.callbackmanager.textcommand.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class TextCommandHandler {
    private final DraimLogin plugin = DraimLogin.getInstance();

    private TextCommandHandler() {
        registerCommand("/help", new HelpTextCommand());
        registerCommand("/start", new StartTextCommand());
        registerCommand("/chatid", new ChatidTextCommand());
        registerCommand("/lock", new LockTextCommand());
        registerCommand("/accountlist", new AccountListTextCommand());
        registerCommand("/unlock", new UnlockTextCommand());
    }

    @Getter(lazy = true)
    private static final TextCommandHandler instance = new TextCommandHandler();

    private final Map<String, AbstractUpdate> commands = new HashMap<>();

    public void run(Update update) {
        String[] args = update.getMessage().getText().split(" ");
        if (!this.isTextCommand(args[0])) return;
        if (!commands.containsKey(args[0])) return;

        commands.get(args[0]).onUpdateCall(plugin.getBot(), update, args);
    }

    private boolean isTextCommand(String text){
        return text.startsWith("/");
    }

    private void registerCommand(String cmd, AbstractUpdate abstractUpdate) {
        commands.put(cmd, abstractUpdate);
    }
}
