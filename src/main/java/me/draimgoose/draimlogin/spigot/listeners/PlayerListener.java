package me.draimgoose.draimlogin.spigot.listeners;

import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.data.PlayerData;
import me.draimgoose.draimlogin.telegram.TelegramBot;
import me.draimgoose.draimlogin.util.*;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PlayerListener implements Listener {
    private final DraimLogin plugin;
    private final PlayerData playerData = PlayerData.getInstance();
    private final TelegramBot bot;

    public PlayerListener(DraimLogin plugin, TelegramBot bot) {
        this.plugin = plugin;
        this.bot = bot;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerExecuteCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (playerData.getPlayerInLogin().containsKey(player.getUniqueId())) {
            String message = event.getMessage().split(" ")[0].toLowerCase();
            if (plugin.getConfig().getBoolean("2FA.enabled") && plugin.getConfig().getStringList("2FA.allowed-commands").contains(message)) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (playerData.getPlayerInLogin().containsKey(player.getUniqueId())) {
            String message = event.getMessage().split(" ")[0].toLowerCase();
            if (plugin.getConfig().getBoolean("2FA.enabled") && plugin.getConfig().getStringList("2FA.allowed-commands").contains(message)) {
                return;
            }
            event.setCancelled(true);
            return;
        }
        if (playerData.getPlayerWaitingForChatid().contains(player.getUniqueId())) {
            event.setCancelled(true);
            String chatId = event.getMessage();
            if (chatId.equalsIgnoreCase("abort")) {
                playerData.getPlayerWaitingForChatid().remove(player.getUniqueId());
                if (plugin.getConfig().getBoolean("2FA.enabled")) {
                    Util.sendPluginMessage(player, PluginMessageAction.REMOVE);
                    player.sendMessage(LangConstants.INGAME_OPERATION_ABORTED.getFormattedString());
                    return;
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> player.kickPlayer(LangConstants.INGAME_KICK_LOG_AGAIN.getFormattedString()), 1);
                return;
            }
            if (!NumberUtils.isNumber(chatId)) {
                player.sendMessage(LangConstants.INGAME_INVALID_VALUE.getFormattedString());
                return;
            }
            try {
                bot.execute(MessageFactory.simpleMessage(chatId, LangConstants.TG_ADD_MESSAGE.getString().replaceAll("%player_name%", player.getName()), KeyboardFactory.addConfirmButtons(player.getUniqueId().toString(), chatId)));
                player.sendMessage(LangConstants.INGAME_WAIT_FOR_CONFIRM.getFormattedString());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (playerData.getPlayerInLogin().containsKey(event.getPlayer().getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(event.getPlayer().getUniqueId())) {
            Location from = event.getFrom();
            Location to = event.getTo();
            double x = Math.floor(from.getX());
            double z = Math.floor(from.getZ());

            if (to == null) {
                return;
            }
            if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z) {
                x += .5;
                z += .5;
                event.getPlayer().teleport(new Location(from.getWorld(), x, from.getY(), z, from.getYaw(), from.getPitch()));
            }

        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (playerData.getPlayerInLogin().containsKey(event.getPlayer().getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerClickOnGui(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (playerData.getPlayerInLogin().containsKey(player.getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerChangeItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (playerData.getPlayerInLogin().containsKey(player.getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerOpenGui(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        if (playerData.getPlayerInLogin().containsKey(player.getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(player.getUniqueId())) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, player::closeInventory, 1);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (playerData.getPlayerInLogin().containsKey(event.getPlayer().getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
        if (playerData.getPlayerInLogin().containsKey(event.getPlayer().getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerPickItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (playerData.getPlayerInLogin().containsKey(player.getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickUpArrow(PlayerPickupArrowEvent event) {
        if (playerData.getPlayerInLogin().containsKey(event.getPlayer().getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (playerData.getPlayerInLogin().containsKey(event.getPlayer().getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (playerData.getPlayerInLogin().containsKey(player.getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (playerData.getPlayerInLogin().containsKey(player.getUniqueId()) || playerData.getPlayerWaitingForChatid().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
