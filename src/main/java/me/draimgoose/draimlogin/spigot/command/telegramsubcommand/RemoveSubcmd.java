package me.draimgoose.draimlogin.spigot.command.telegramsubcommand;

import me.draimgoose.draimlogin.spigot.command.SubCommand;
import me.draimgoose.draimlogin.util.LangConstants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 1){
            if (!(sender instanceof Player)){
                sender.sendMessage(LangConstants.INGAME_ONLY_PLAYER.getFormattedString());
                return;
            }
            Player player = (Player) sender;
            if (!plugin.getConfig().getBoolean("2FA.enabled")){
                sender.sendMessage(LangConstants.INGAME_CANNOT_REMOVE.getFormattedString());
                return;
            }
            if (!playerData.getPlayerCache().containsKey(player.getUniqueId())){
                sender.sendMessage(LangConstants.INGAME_SENDER_WITHOUT_DRAIMLOGIN.getFormattedString());
                return;
            }
            playerData.getPlayerCache().remove(player.getUniqueId());
            plugin.getSql().removePlayerLogin(player.getUniqueId().toString());
            player.sendMessage(LangConstants.INGAME_ACCOUNT_DISCONNECTED.getFormattedString());
            return;
        }
        if (args.length > 1){
            if (!sender.hasPermission("draimlogin.admin")) {
                sender.sendMessage(LangConstants.INGAME_NOPERMISSION.getFormattedString());
                return;
            }
            if (args.length != 2) {
                sender.sendMessage(LangConstants.INGAME_REMOVE_USAGE.getFormattedString());
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(LangConstants.INGAME_TARGET_OFFLINE.getFormattedString());
                return;
            }

            playerData.getPlayerCache().remove(target.getUniqueId());

            plugin.getSql().getTelegramPlayer(target.getUniqueId().toString()).whenComplete((telegramPlayer, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                }
            }).thenAccept(telegramPlayer -> {
                if (telegramPlayer != null) {
                    plugin.getSql().removePlayerLogin(target.getUniqueId().toString());
                    sender.sendMessage(LangConstants.INGAME_TARGET_REMOVED.getFormattedString().replaceAll("%target%", target.getName()));
                } else {
                    sender.sendMessage(LangConstants.INGAME_TARGET_WITHOUT_DRAIM_LOGIN.getFormattedString());
                }
            });
        }
    }
}
