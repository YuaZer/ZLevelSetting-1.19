package io.github.yuazer.zlevelsetting.Utils;

import io.github.yuazer.zlevelsetting.Main;

import java.util.List;

public class YamlUtils {
    public static String getConfigMessage(String path) {
        return Main.getInstance().getConfig().getString(path).replace("&", "§");
    }

    public static int getConfigInt(String path) {
        return Main.getInstance().getConfig().getInt(path);
    }

    public static boolean getConfigBoolean(String path) {
        return Main.getInstance().getConfig().getBoolean(path);
    }

    public static List<String> getConfigStringList(String path) {
        return Main.getInstance().getConfig().getStringList(path);
    }

}
