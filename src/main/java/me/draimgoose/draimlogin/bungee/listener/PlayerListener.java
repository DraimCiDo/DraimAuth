package me.draimgoose.draimlogin.bungee.listener;

import me.draimgoose.draimlogin.data.PlayerData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onCommand(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if (event.isProxyCommand()) {
            if (PlayerData.getInstance().getBungeePendingPlayers().contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        PlayerData.getInstance().getBungeePendingPlayers().remove(event.getPlayer().getUniqueId());
    }

}
