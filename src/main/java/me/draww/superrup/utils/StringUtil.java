package me.draww.superrup.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.draww.superrup.Main;
import org.bukkit.entity.Player;

public class StringUtil {

    public static String replacePlayerPlaceholders(Player paramPlayer, String paramString) {
        if ((paramString == null) || (paramString.isEmpty())) {
            return paramString;
        }
        paramString = Text.colorize(paramString);
        if (!paramString.contains("%")) {
            return paramString;
        }
        if (Main.INSTANCE.controlPlaceholderAPI()) {
            paramString = PlaceholderAPI.setPlaceholders(paramPlayer, paramString);
        }
        return paramString;
    }

}
