package me.draimgoose.draimlogin.spigot.command.telegramsubcommand;

import me.draimgoose.draimlogin.spigot.command.SubCommand;
import me.draimgoose.draimlogin.util.LangConstants;
import org.bukkit.command.CommandSender;

public class HelpSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(LangConstants.INGAME_HELP_PLAYER.getFormattedString());

        if (sender.hasPermission("draimlogin.admin")) {
            sender.sendMessage(LangConstants.INGAME_HELP_ADMIN.getFormattedString());
        }
    }
}

