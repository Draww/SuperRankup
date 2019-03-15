package me.draww.superrup.condition;

import me.draww.superrup.Main;
import me.draww.superrup.utils.StringUtil;
import me.draww.superrup.utils.Text;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.BiPredicate;

public enum ConditionType {
    MONEY((player, condition) -> {
        if (!condition.getRequiredDataSection().contains("value") || !condition.getRequiredDataSection().isDouble("value")) return false;
        double data = condition.getRequiredDataSection().getDouble("value");
        double money = Main.getInstance().getVaultEconomy().getBalance(player);
        if (Double.compare(money, data) == -1) {
            message(player, condition.getMessage(), condition);
            return false;
        }
        return true;
    }),
    EXP((player, condition) -> {
        if (!condition.getRequiredDataSection().contains("value") || !condition.getRequiredDataSection().isInt("value")) return false;
        Integer data = condition.getRequiredDataSection().getInt("value");
        if (player.getExp() != data) {
            message(player, condition.getMessage(), condition);
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

    private static void message(Player player, String message, Condition condition) {
        Map<String, Object> replaceData = condition.getRequiredDataSection().getValues(true);
        if (replaceData != null) {
            for (Map.Entry<String, Object> entry : replaceData.entrySet()) {
                message = message.replace("%" + entry.getKey().toLowerCase() + "%", String.valueOf(entry.getValue()));
            }
        }
        message = StringUtil.replacePlayerPlaceholders(player, message
                .replace("%rank%", condition.getRank().getId())
                .replace("%rank_group%", condition.getRank().getGroup()));
        player.sendMessage(Text.colorize(message));
    }
}
