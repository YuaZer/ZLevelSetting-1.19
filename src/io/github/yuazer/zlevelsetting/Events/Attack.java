package io.github.yuazer.zlevelsetting.Events;

import io.github.yuazer.zlevelsetting.Main;
import io.github.yuazer.zlevelsetting.Utils.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Attack implements Listener {
    @EventHandler
    public void onAttackPlayer(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player attacker = Bukkit.getPlayer(e.getDamager().getUniqueId());
            Player victim = Bukkit.getPlayer(e.getEntity().getUniqueId());
            if (attacker==null||victim==null){
                return;
            }
            if (attacker.getLevel()<=YamlUtils.getConfigInt("Protect.newplayer")||victim.getLevel()<=YamlUtils.getConfigInt("Protect.newplayer")){
                e.setCancelled(true);
                attacker.sendMessage(YamlUtils.getConfigMessage("Message.newPlayerProtect"));
                victim.sendMessage(YamlUtils.getConfigMessage("Message.newPlayerProtect"));
                return;
            }
            int levelDifference = Math.abs(attacker.getLevel() - victim.getLevel());
            if (levelDifference > Main.getInstance().getConfig().getInt("Protect.playerlimit")) {
                e.setCancelled(true);
                attacker.sendMessage(YamlUtils.getConfigMessage("Message.LevelDifference"));
                victim.sendMessage(YamlUtils.getConfigMessage("Message.LevelDifference"));
            }
        }
    }
}
