package me.draimgoose.draimlogin.spigot.command;

import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.data.PlayerData;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    protected final DraimLogin plugin = DraimLogin.getInstance();
    protected final PlayerData playerData = PlayerData.getInstance();
    public abstract void onCommand(CommandSender sender, String[] args);
}
