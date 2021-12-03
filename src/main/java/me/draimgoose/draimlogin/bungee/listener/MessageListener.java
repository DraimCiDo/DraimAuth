package me.draimgoose.draimlogin.bungee.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.draimgoose.draimlogin.data.PlayerData;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class MessageListener implements Listener {

    @EventHandler
    @SuppressWarnings("UnstableApiUsage")
    public void onMessageReceived(PluginMessageEvent event) {
        if (!(event.getTag().equalsIgnoreCase("hxj:telegramlogin"))
                || !(event.getSender() instanceof Server))
            return;

        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());

        String subChannel = input.readUTF();
        if (!subChannel.equals("cache")) return;
        String action = input.readUTF();
        UUID playerUUID = UUID.fromString(input.readUTF());
        switch (action.toLowerCase()) {
            case "add": {
                PlayerData.getInstance().getBungeePendingPlayers().add(playerUUID);
                break;
            }
            case "remove": {
                PlayerData.getInstance().getBungeePendingPlayers().remove(playerUUID);
                break;
            }
            default:
                return;
        }
    }

}
