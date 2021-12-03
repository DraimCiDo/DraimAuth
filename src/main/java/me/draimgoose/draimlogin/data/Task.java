package me.draimgoose.draimlogin.data;

import lombok.RequiredArgsConstructor;
import me.draimgoose.draimlogin.DraimLogin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@RequiredArgsConstructor
public class Task {
    private final DraimLogin plugin;

    public void startClearCacheTask(){
        long l = 20 * 60 * 20;  //20 minutes
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : PlayerData.getInstance().getPlayerCache().keySet()){
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null)
                        PlayerData.getInstance().getPlayerCache().remove(uuid);
                }
            }
        },l,l);
    }


}
