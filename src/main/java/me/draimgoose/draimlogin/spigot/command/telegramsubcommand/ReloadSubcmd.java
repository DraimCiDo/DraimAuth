package me.draimgoose.draimlogin.spigot.command.telegramsubcommand;

import me.draimgoose.draimlogin.spigot.command.SubCommand;
import me.draimgoose.draimlogin.util.LangConstants;
import org.bukkit.command.CommandSender;

public class ReloadSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("draimlogin.admin")) {
            sender.sendMessage(LangConstants.INGAME_NOPERMISSION.getFormattedString());
            return;
        }
        plugin.reloadConfig();
        plugin.loadLangConfig();
        sender.sendMessage(LangConstants.INGAME_RELOAD_EXECUTED.getFormattedString());
    }
}
