package me.draww.superrup.condition;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.BiPredicate;

public enum ConditionType {
    MONEY((player, condition) -> {
        Integer data = (Integer) condition.getRequiredData().get("value");
        //TODO: Get player money and control it
        return true;
    }),
    EXP((player, condition) -> {
        Float data = (Float) condition.getRequiredData().get("value");
        if (player.getExp() != data) {
            message(player, condition.getMessage(), condition.getRequiredData());
            return false;
        }
        return true;
    });

    private BiPredicate<Player, Condition> predicate;

    ConditionType(BiPredicate<Player, Condition> predicate) {
        this.predicate = predicate;
    }

    public BiPredicate<Player, Condition> getPredicate() {
        return predicate;
    }

    private static void message(Player player, String message, Map<String, Object> replaceData) {
        if (replaceData != null) {
            for (Map.Entry<String, Object> entry : replaceData.entrySet()) {
                message = message.replace("{" + entry.getKey().toLowerCase() + "}", String.valueOf(entry.getValue()));
            }
        }
        player.sendMessage(message);
    }
}
