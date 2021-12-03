package me.draimgoose.draimlogin.spigot.command.telegramsubcommand;

import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.spigot.command.SubCommand;
import me.draimgoose.draimlogin.util.LangConstants;
import me.draimgoose.draimlogin.util.TelegramPlayer;
import me.draimgoose.draimlogin.util.TelegramPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AccountsSubcmd extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LangConstants.INGAME_ONLY_PLAYER.getFormattedString());
                return;
            }

            Player player = (Player) sender;
            TelegramPlayer telegramPlayer = playerData.getPlayerCache().get(player.getUniqueId());

            String chatId = telegramPlayer.getChatID();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LangConstants.INGAME_ACCOUNT_LIST.getFormattedString() + "\n");

            DraimLogin.getInstance().getSql().getTelegramPlayerInfoList(chatId).whenComplete((telegramPlayers, throwable) -> {
                if (throwable != null)
                    throwable.printStackTrace();
            }).thenAccept(telegramPlayers -> {
                if (!telegramPlayers.isEmpty()) {
                    for (TelegramPlayerInfo tp : telegramPlayers) {
                        String status = tp.isLocked() ? LangConstants.INGAME_ACCOUNT_LIST_LOCKED.getFormattedString() : LangConstants.INGAME_ACCOUNT_LIST_UNLOCKED.getFormattedString();

                        stringBuilder.append(LangConstants.INGAME_ACCOUNT_LIST_SINTAX.getFormattedString()
                                .replaceAll("%accountID%", String.valueOf(tp.getAccountId()))
                                .replaceAll("%player_name%", tp.getPlayerName())
                                .replaceAll("%status%", status)
                        );
                        stringBuilder.append("\n");
                    }
                }
                player.sendMessage(stringBuilder.toString());
            });
        } else if (args.length >= 2) {
            if (!sender.hasPermission("draimlogin.admin")) {
                sender.sendMessage(LangConstants.INGAME_NOPERMISSION.getFormattedString());
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                if (playerData.getPlayerCache().containsKey(target.getUniqueId())) {
                    TelegramPlayer telegramPlayer = playerData.getPlayerCache().get(target.getUniqueId());
                    String chatId = telegramPlayer.getChatID();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(LangConstants.INGAME_ACCOUNT_LIST.getFormattedString() + "\n");

                    DraimLogin.getInstance().getSql().getTelegramPlayerInfoList(chatId).whenComplete((telegramPlayers, throwable) -> {
                        if (throwable != null)
                            throwable.printStackTrace();
                    }).thenAccept(telegramPlayers -> {
                        if (!telegramPlayers.isEmpty()) {
                            for (TelegramPlayerInfo tp : telegramPlayers) {
                                String status = tp.isLocked() ? LangConstants.INGAME_ACCOUNT_LIST_LOCKED.getFormattedString() : LangConstants.INGAME_ACCOUNT_LIST_UNLOCKED.getFormattedString();

                                stringBuilder.append(LangConstants.INGAME_ACCOUNT_LIST_SINTAX.getFormattedString()
                                        .replaceAll("%accountID%", String.valueOf(tp.getAccountId()))
                                        .replaceAll("%player_name%", tp.getPlayerName())
                                        .replaceAll("%status%", status)
                                );
                                stringBuilder.append("\n");
                            }
                        }
                        sender.sendMessage(stringBuilder.toString());
                    });
                    return;
                } else {
                    sender.sendMessage(LangConstants.INGAME_TARGET_WITHOUT_DRAIM_LOGIN.getFormattedString());
                    return;
                }
            } else {
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);
                if (playerData.getPlayerCache().containsKey(offlineTarget.getUniqueId())) {
                    TelegramPlayer telegramPlayer = playerData.getPlayerCache().get(offlineTarget.getUniqueId());
                    String chatId = telegramPlayer.getChatID();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(LangConstants.INGAME_ACCOUNT_LIST.getFormattedString() + "\n");

                    DraimLogin.getInstance().getSql().getTelegramPlayerInfoList(chatId).whenComplete((telegramPlayers, throwable) -> {
                        if (throwable != null)
                            throwable.printStackTrace();
                    }).thenAccept(telegramPlayers -> {
                        if (!telegramPlayers.isEmpty()) {
                            for (TelegramPlayerInfo tp : telegramPlayers) {
                                String status = tp.isLocked() ? LangConstants.INGAME_ACCOUNT_LIST_LOCKED.getFormattedString() : LangConstants.INGAME_ACCOUNT_LIST_UNLOCKED.getFormattedString();

                                stringBuilder.append(LangConstants.INGAME_ACCOUNT_LIST_SINTAX.getFormattedString()
                                        .replaceAll("%accountID%", String.valueOf(tp.getAccountId()))
                                        .replaceAll("%player_name%", tp.getPlayerName())
                                        .replaceAll("%status%", status)
                                );
                                stringBuilder.append("\n");
                            }
                        }
                        sender.sendMessage(stringBuilder.toString());
                    });
                    return;
                } else {
                    plugin.getSql().getTelegramPlayer(offlineTarget.getUniqueId().toString()).whenComplete((telegramPlayer, throwable) -> {
                        if (throwable != null)
                            throwable.printStackTrace();
                    }).thenAccept(telegramPlayer -> {
                        if (telegramPlayer == null) {
                            sender.sendMessage(LangConstants.INGAME_TARGET_WITHOUT_DRAIM_LOGIN.getFormattedString());
                            return;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(LangConstants.INGAME_ACCOUNT_LIST.getFormattedString() + "\n");

                            DraimLogin.getInstance().getSql().getTelegramPlayerInfoList(telegramPlayer.getChatID()).whenComplete((telegramPlayers, throwable) -> {
                                if (throwable != null)
                                    throwable.printStackTrace();
                            }).thenAccept(telegramPlayers -> {
                                if (!telegramPlayers.isEmpty()) {
                                    for (TelegramPlayerInfo tp : telegramPlayers) {
                                        String status = tp.isLocked() ? LangConstants.INGAME_ACCOUNT_LIST_LOCKED.getFormattedString() : LangConstants.INGAME_ACCOUNT_LIST_UNLOCKED.getFormattedString();

                                        stringBuilder.append(LangConstants.INGAME_ACCOUNT_LIST_SINTAX.getFormattedString()
                                                .replaceAll("%accountID%", String.valueOf(tp.getAccountId()))
                                                .replaceAll("%player_name%", tp.getPlayerName())
                                                .replaceAll("%status%", status)
                                        );
                                        stringBuilder.append("\n");
                                    }
                                }
                                sender.sendMessage(stringBuilder.toString());
                            });

                        }
                    });
                }
            }
        }
    }
}

