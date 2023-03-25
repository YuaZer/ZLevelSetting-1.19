package io.github.yuazer.zlevelsetting.Events;

import io.github.yuazer.zlevelsetting.Main;
import io.github.yuazer.zlevelsetting.Utils.YamlUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
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
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, cmd.replace("@getLevel@", String.valueOf(i + 1))));
                }
            }
        }
        if (YamlUtils.getConfigBoolean("Events.OnSpecialLevelUp.enable")) {
            for (int i = e.getOldLevel(); i < e.getNewLevel(); i++) {
                if (Main.getInstance().getConfig().getConfigurationSection("Events.OnSpecialLevelUp").getKeys(false).contains(String.valueOf(i + 1))) {
                    for (String cmd : YamlUtils.getConfigStringList("Events.OnSpecialLevelUp." + (i + 1))) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, cmd.replace("@getLevel@", String.valueOf(i + 1))));
                    }
                }
            }
        }
    }

    /**
     * 限制每日获取经验
     */
    @EventHandler
    public void onExpGet(PlayerExpChangeEvent e) {
        if (Main.getInstance().getConfig().getBoolean("Max-Exp-Limit.enable")) {
            Player player = e.getPlayer();
            int getAmount = e.getAmount();
            //遍历特殊权限列表
            for (String pm : Main.getInstance().getConfig().getConfigurationSection("Max-Exp-Limit.SpecialPerm").getKeys(false)) {
                String pm1 = "ZLevelSetting.speciallimit." + pm;
                //判断玩家是否有权限
                if (player.hasPermission(pm1)) {
                    int limit = Main.getInstance().getConfig().getInt("Max-Exp-Limit.SpecialPerm." + pm);
                    if (limitExp(e, player, getAmount, limit)) return;
                } else {
                    break;
                }
            }
            int limit = Main.getInstance().getConfig().getInt("Max-Exp-Limit.default");
            if (limitExp(e, player, getAmount, limit)) return;
            Main.getDailyExp().put(player.getUniqueId(), Main.getDailyExp().getOrDefault(player.getUniqueId(), 0) + getAmount);
        }
    }

    private boolean limitExp(PlayerExpChangeEvent e, Player player, int getAmount, int limit) {
        if (Main.getDailyExp().getOrDefault(player.getUniqueId(), 0) + getAmount > limit) {
            if (Main.getDailyExp().getOrDefault(player.getUniqueId(), 0) >= limit) {
                e.setAmount(0);
            } else {
                e.setAmount(limit - Main.getDailyExp().getOrDefault(player.getUniqueId(), 0) + getAmount);
                Main.getDailyExp().put(player.getUniqueId(), Main.getDailyExp().getOrDefault(player.getUniqueId(), 0) + getAmount);
            }
//            player.sendMessage(YamlUtils.getConfigMessage("Message.atLimitExp"));
            sendActionBar(player,YamlUtils.getConfigMessage("Message.atLimitExp"));
            return true;
        }
        return false;
    }
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
