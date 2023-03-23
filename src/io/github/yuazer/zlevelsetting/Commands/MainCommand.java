package io.github.yuazer.zlevelsetting.Commands;

import io.github.yuazer.zlevelsetting.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("zlevelsetting")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§b/zlevelsetting §a简写-> §b/zlls");
                sender.sendMessage("§b/zlevelsetting reload §a重载配置文件");
                return true;
            }
            if (args[0].equalsIgnoreCase("tell") && sender.isOp()) {
                Player player = Bukkit.getPlayer(args[1]);
                StringBuffer buffer = new StringBuffer();
                for (int i = 2; i < args.length; i++) {
                    buffer.append(args[i].replace("&", "§")).append(" ");
                }
                player.sendMessage(buffer.toString());
                return true;
            }
            if (args[0].equalsIgnoreCase("reload") && sender.isOp()) {
                Main.getInstance().reloadConfig();
                Main.setMaxLevel(Main.getInstance().getConfig().getInt("custom-levels.max-level"));
                sender.sendMessage(Main.getInstance().getConfig().getString("Message.reload").replace("&", "§"));
                return true;
            }
        }
        return false;
    }
}
