package me.draww.superrup.condition;

import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConditionProvider {

    public static Map<String, Condition> deserializeConditions(ConfigurationSection section, Rank rank) {
        if (section == null) return new HashMap<>();
        if (section.contains("template") && section.isString("template")) {
            ConfigurationSection templateSection = Main.getInstance().getTemplateConfig().getConfigurationSection("conditions." + section.getString("template"));
            return deserializeConditions(templateSection, rank);
        }
        Map<String, Condition> conditionMap = new HashMap<>();
        for (String condKey : section.getKeys(false)) {
            if ((!section.contains(condKey + ".type") && ! section.isString(condKey + ".type"))
                    || (!section.contains(condKey + ".message") && ! section.isString(condKey + ".message"))) continue;
            conditionMap.put(condKey, new Condition(condKey,
                    rank,
                    section.getConfigurationSection(condKey + ".data").getValues(true),
                    section.getString(condKey + ".message"),
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
