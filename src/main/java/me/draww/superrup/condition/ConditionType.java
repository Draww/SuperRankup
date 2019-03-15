package me.draww.superrup.condition;

import me.draww.superrup.Main;
import me.draww.superrup.utils.StringUtil;
import me.draww.superrup.utils.Text;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.BiPredicate;

public enum ConditionType {
    MONEY((player, condition) -> {
        double data;
        try {
            data = (double) condition.getRequiredData().get("value");
        } catch (Exception e) {
            return false;
        }
        double money = Main.getInstance().getVaultEconomy().getBalance(player);
        if (Double.compare(money, data) == -1) {
            message(player, condition.getMessage(), condition);
            return false;
        }
        return true;
    }),
    EXP((player, condition) -> {
        Integer data;
        try {
            data = (Integer) condition.getRequiredData().get("value");
        } catch (Exception e) {
            return false;
        }
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
        Map<String, Object> replaceData = condition.getRequiredData();
        if (replaceData != null) {
            for (Map.Entry<String, Object> entry : replaceData.entrySet()) {
                message = message.replace("%" + entry.getKey().toLowerCase() + "%", String.valueOf(entry.getValue()));
            }
        }
        message = StringUtil.replacePlayerPlaceholders(player, message
                .replace("%rank%", condition.getRank().getId()));
        player.sendMessage(Text.colorize(message));
    }
}
