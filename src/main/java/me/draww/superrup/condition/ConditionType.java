package me.draww.superrup.condition;

import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import me.draww.superrup.utils.TriPredicate;
import org.bukkit.entity.Player;

import java.util.Map;

public enum ConditionType {
    MONEY((player, condition, rank) -> {
        Double data = (Double) condition.getRequiredData().get("value");
        Double money = Main.getInstance().getVaultEconomy().getBalance(player);
        if (Double.compare(money, data) == -1) { // Money < Data Value == True
            message(player, condition.getMessage(), condition.getRequiredData());
            return false;
        }
        return true;
    }),
    EXP((player, condition, rank) -> {
        Float data = (Float) condition.getRequiredData().get("value");
        if (player.getExp() != data) {
            message(player, condition.getMessage(), condition.getRequiredData());
            return false;
        }
        return true;
    });

    private TriPredicate<Player, Condition, Rank> predicate;

    ConditionType(TriPredicate<Player, Condition, Rank> predicate) {
        this.predicate = predicate;
    }

    public TriPredicate<Player, Condition, Rank> getPredicate() {
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
