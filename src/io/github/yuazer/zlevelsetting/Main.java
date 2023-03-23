package io.github.yuazer.zlevelsetting;

import io.github.yuazer.zlevelsetting.Commands.MainCommand;
import io.github.yuazer.zlevelsetting.Events.Exp;
import io.github.yuazer.zlevelsetting.Hook.PAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    private static int maxLevel;

    public static void setMaxLevel(int maxLevel) {
        Main.maxLevel = maxLevel;
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
        logLoaded(this);
        Bukkit.getPluginManager().registerEvents(new Exp(), this);
        Bukkit.getPluginCommand("zlevelsetting").setExecutor(new MainCommand());
        saveDefaultConfig();
        PAPIHook papiHook = new PAPIHook();
        if (papiHook.canRegister()) {
            papiHook.register();
        }
    }
    public void onDisable() {
        logDisable(this);
        PAPIHook papiHook = new PAPIHook();
        papiHook.unregister();
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
