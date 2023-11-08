package io.github.yuazer.zlevelsetting;

import io.github.yuazer.zlevelsetting.Commands.MainCommand;
import io.github.yuazer.zlevelsetting.Events.Attack;
import io.github.yuazer.zlevelsetting.Events.Exp;
import io.github.yuazer.zlevelsetting.Events.PlayerEvents;
import io.github.yuazer.zlevelsetting.Hook.PAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {
    private static Main instance;
    private static int maxLevel;
    private static FileConfiguration maxExpLimitConf;
    private static Map<UUID, Integer> dailyExp = new HashMap<>();

    public static Map<UUID, Integer> getDailyExp() {
        return dailyExp;
    }

    public static void setMaxLevel(int maxLevel) {
        Main.maxLevel = maxLevel;
    }

    public static void saveMaxExpLimitConf(FileConfiguration maxExpConf) {
        try {
            File file = new File("plugins/ZLevelSetting/ExpLimit.yml");
            maxExpConf.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileConfiguration getMaxExpLimitConf() {
        return maxExpLimitConf;
    }

    public static int getMaxLevel() {
        return maxLevel;
    }


    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        maxLevel = getConfig().getInt("custom-levels.max-level");
        saveResource("ExpLimit.yml", false);
        maxExpLimitConf = YamlConfiguration.loadConfiguration(new File("plugins/ZLevelSetting/ExpLimit.yml"));
        InitLimit();
        logLoaded(this);
        Bukkit.getPluginManager().registerEvents(new Exp(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
        Bukkit.getPluginManager().registerEvents(new Attack(), this);
        Bukkit.getPluginCommand("zlevelsetting").setExecutor(new MainCommand());
        saveDefaultConfig();
        PAPIHook papiHook = new PAPIHook();
        if (papiHook.canRegister()) {
            papiHook.register();
        }
        // Schedule daily reset task
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        getServer().getScheduler().runTaskTimerAsynchronously(this, this::resetDailyExp,
                midnight.getTimeInMillis() - System.currentTimeMillis(), 24 * 60 * 60 * 20);
    }

    private void resetDailyExp() {
        try {
            for (Map.Entry<UUID, Integer> entry : dailyExp.entrySet()) {
                entry.setValue(maxExpLimitConf.getInt(entry.getKey().toString(), 0));
            }
            for (String uid : getMaxExpLimitConf().getKeys(false)) {
                getMaxExpLimitConf().set(uid, 0);
            }
            saveMaxExpLimitConf(getMaxExpLimitConf());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void InitLimit() {
        try {
            for (String uuid : getMaxExpLimitConf().getKeys(false)) {
                getDailyExp().put(UUID.fromString(uuid), getMaxExpLimitConf().getInt(uuid));
            }
        } catch (NullPointerException ignored) {
        }
    }

    public void onDisable() {
        logDisable(this);
        PAPIHook papiHook = new PAPIHook();
        papiHook.unregister();
        for (UUID key : getDailyExp().keySet()) {
            getMaxExpLimitConf().set(String.valueOf(key), dailyExp.getOrDefault(key, 0));
        }
        saveMaxExpLimitConf(getMaxExpLimitConf());
    }

    public static void logLoaded(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §f已加载", plugin.getName()));
        Bukkit.getLogger().info("§b作者:§eZ菌");
        Bukkit.getLogger().info("§b版本:§e" + plugin.getDescription().getVersion());
    }

    public static void logDisable(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §c已卸载", plugin.getName()));
    }
}
