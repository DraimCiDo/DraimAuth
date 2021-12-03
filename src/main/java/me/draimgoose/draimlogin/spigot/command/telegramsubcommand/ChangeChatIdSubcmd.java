package me.draimgoose.draimlogin.spigot.command.telegramsubcommand;

import me.draimgoose.draimlogin.spigot.command.SubCommand;
import me.draimgoose.draimlogin.util.LangConstants;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeChatIdSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("draimlogin.admin")) {
            sender.sendMessage(LangConstants.INGAME_NOPERMISSION.getFormattedString());
            return;
        }
        if (args.length != 3) {
            sender.sendMessage(LangConstants.INGAME_CHANGE_CHATID_USAGE.getFormattedString());
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(LangConstants.INGAME_TARGET_OFFLINE.getFormattedString());
            return;
        }
        if (!NumberUtils.isDigits(args[2])) {
            sender.sendMessage(LangConstants.INGAME_INVALID_VALUE.getFormattedString());
            return;
        }

        if (playerData.getPlayerCache().containsKey(target.getUniqueId())) {
            playerData.getPlayerCache().get(target.getUniqueId()).setChatID(args[2]);
        }
        plugin.getSql().getTelegramPlayer(target.getUniqueId().toString()).whenComplete((telegramPlayer, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }).thenAccept(telegramPlayer -> {
            if (telegramPlayer != null) {
                plugin.getSql().setChatId(target.getUniqueId().toString(), args[2]);
                sender.sendMessage(LangConstants.INGAME_CHATID_CHANGED.getFormattedString());
            } else {
                sender.sendMessage(LangConstants.INGAME_TARGET_WITHOUT_DRAIM_LOGIN.getFormattedString());
            }
        });
    }
}
