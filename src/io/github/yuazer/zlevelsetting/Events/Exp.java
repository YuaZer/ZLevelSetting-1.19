package io.github.yuazer.zlevelsetting.Events;

import io.github.yuazer.zlevelsetting.Main;
import io.github.yuazer.zlevelsetting.Utils.YamlUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class Exp implements Listener {
    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent e) {
        //TODO 最高等级限制
        Player player = e.getPlayer();
        if (Main.getInstance().getConfig().getConfigurationSection("Max-Level-Setting").getKeys(false).contains(String.valueOf(e.getNewLevel()))) {
            if (!player.hasPermission(YamlUtils.getConfigMessage("Max-Level-Setting." + e.getNewLevel()))) {
                player.setLevel(e.getOldLevel());
                player.sendMessage(YamlUtils.getConfigMessage("Message.noPermToLevelUp"));
                return;
            }
        }
        if (e.getNewLevel() >= Main.getMaxLevel()) {
            player.setLevel(e.getOldLevel());
            player.sendMessage(YamlUtils.getConfigMessage("Message.atMaxLevel"));
            return;
        }
        if (e.getNewLevel() < e.getOldLevel()) {
            player.setLevel(e.getOldLevel());
            player.sendMessage(YamlUtils.getConfigMessage("Message.errorLevel"));
            return;
        }
        if (YamlUtils.getConfigBoolean("Events.OnEveryLevelUp.enable")) {
            for (int i = e.getOldLevel(); i < e.getNewLevel(); i++) {
                for (String cmd : YamlUtils.getConfigStringList("Events.OnEveryLevelUp.commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, cmd.replace("@getLevel@",String.valueOf(i+1))));
                }
            }
        }
        if (YamlUtils.getConfigBoolean("Events.OnSpecialLevelUp.enable")) {
            for (int i = e.getOldLevel();i<e.getNewLevel();i++){
                if (Main.getInstance().getConfig().getConfigurationSection("Events.OnSpecialLevelUp").getKeys(false).contains(String.valueOf(i+1))) {
                    for (String cmd : YamlUtils.getConfigStringList("Events.OnSpecialLevelUp." + (i + 1))) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, cmd.replace("@getLevel@",String.valueOf(i+1))));
                    }
                }
            }
        }
    }
}
