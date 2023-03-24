package io.github.yuazer.zlevelsetting.Events;

import io.github.yuazer.zlevelsetting.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        //Map存玩家UUID,已获取经验值
        Main.getDailyExp().put(player.getUniqueId(), Main.getMaxExpLimitConf().getInt(player.getUniqueId().toString()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Main.getMaxExpLimitConf().set(String.valueOf(player.getUniqueId()), Main.getDailyExp().getOrDefault(player.getUniqueId(), 0));
        Main.saveMaxExpLimitConf(Main.getMaxExpLimitConf());
    }
}