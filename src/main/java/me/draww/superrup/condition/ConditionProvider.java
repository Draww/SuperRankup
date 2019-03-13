package me.draww.superrup.condition;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionProvider {

    public static Map<String, Condition> deserializeConditions(ConfigurationSection section) {
        Map<String, Condition> conditionMap = new HashMap<>();
        for (String condKey : section.getKeys(false)) {
            if ((!section.contains(condKey + ".type") && ! section.isString(condKey + ".type"))
                    || (!section.contains(condKey + ".message") && ! section.isString(condKey + ".message"))) continue;
            conditionMap.put(condKey, new Condition(condKey,
                    section.getConfigurationSection(condKey + ".data").getValues(false),
                    ConditionType.valueOf(section.getString(condKey + ".type").toUpperCase())));
        }
        return conditionMap;
    }

    public static boolean testAllConditions(Player player, List<Condition> conditions) {
        for (Condition condition : conditions) {
            if (!condition.test(player)) {
                return false;
            }
        }
        return true;
    }

}
