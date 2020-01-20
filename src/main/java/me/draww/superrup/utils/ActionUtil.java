package me.draww.superrup.utils;

import me.draww.superrup.api.Condition;
import org.bukkit.entity.Player;

public class ActionUtil {

    public static void message(Player player, String message, Condition condition) {
        message = Text.replacePlayerPlaceholders(player, message
                .replace("%rank%", condition.rank().getId())
                .replace("%rank_group%", condition.rank().getGroup()));
        player.sendMessage(Text.colorize(message));
    }

}
