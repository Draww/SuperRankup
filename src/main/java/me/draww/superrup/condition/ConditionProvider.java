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
        Map<String, Condition> conditionMap = new HashMap<>();
        for (String condKey : section.getKeys(false)) {
            if (section.contains(condKey + ".template") && section.isString(condKey + ".template")) {
                ConfigurationSection templateSection = Main.getInstance().getTemplateConfig().getConfigurationSection("conditions." + section.getString(condKey + ".template"));
                if ((!templateSection.contains("type") && !templateSection.isString("type"))
                        || (!templateSection.contains("message") && !templateSection.isString("message"))
                        || (!section.contains(condKey + ".queue") && !section.isInt(condKey + ".queue"))) continue;
                conditionMap.put(condKey, new Condition(condKey,
                        section.getInt(condKey + ".queue"),
                        rank,
                        templateSection.getConfigurationSection("data").getValues(true),
                        templateSection.getString("message"),
                        ConditionType.valueOf(templateSection.getString("type").toUpperCase())));
            } else {
                if ((!section.contains(condKey + ".type") && !section.isString(condKey + ".type"))
                        || (!section.contains(condKey + ".message") && !section.isString(condKey + ".message"))
                        || (!section.contains(condKey + ".queue") && !section.isString(condKey + ".queue"))) continue;
                conditionMap.put(condKey, new Condition(condKey,
                        section.getInt(condKey + ".queue"),
                        rank,
                        section.getConfigurationSection(condKey + ".data").getValues(true),
                        section.getString(condKey + ".message"),
                        ConditionType.valueOf(section.getString(condKey + ".type").toUpperCase())));
            }
        }
        return conditionMap;
    }

    public static boolean testAllConditions(Player player, Rank rank) {
        for (Condition condition : rank.getSortedConditions()) {
            if (!condition.test(player)) {
                return false;
            }
        }
        return true;
    }

}
