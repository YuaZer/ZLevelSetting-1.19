package io.github.yuazer.zlevelsetting.Hook;

import io.github.yuazer.zlevelsetting.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "zlevelsetting";
    }

    @Override
    public String getAuthor() {
        return "Z菌";
    }

    @Override
    public String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("required")) {
        }
        return "error";
    }
}
