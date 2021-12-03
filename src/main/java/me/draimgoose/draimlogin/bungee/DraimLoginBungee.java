package me.draimgoose.draimlogin.bungee;

import lombok.Getter;
import me.draimgoose.draimlogin.bungee.listener.MessageListener;
import me.draimgoose.draimlogin.bungee.listener.PlayerListener;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class DraimLoginBungee extends Plugin {
    @Getter
    private static DraimLoginBungee instance;

    @Override
    public void onEnable() {
        instance = this;
        this.loadListeners(new PlayerListener(), new MessageListener());
        this.getProxy().registerChannel("hxj:draimlogin");
    }

    @Override
    public void onDisable() {
        //
    }

    private void loadListeners(Listener... listeners) {
        for (Listener l : listeners) {
            this.getProxy().getPluginManager().registerListener(this, l);
        }
    }

}
