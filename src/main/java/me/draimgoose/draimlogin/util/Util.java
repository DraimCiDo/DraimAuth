package me.draimgoose.draimlogin.util;

import me.draimgoose.draimlogin.DraimLogin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Util {
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void loadListeners(DraimLogin plugin, Listener... listeners) {
        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, plugin);
        }
    }

    public static void sendPluginMessage(Player player, PluginMessageAction action) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("cache");
            out.writeUTF(action.getType());
            out.writeUTF(player.getUniqueId().toString());

            player.sendPluginMessage(DraimLogin.getInstance(), "hxj:draimlogin", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
