package me.draww.superrup.executor;

import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutorProvider {

    public static Map<String, Executor> deserializeExecutors(ConfigurationSection section, Rank rank) {
        if (section == null) return new HashMap<>();
        Map<String, Executor> executorMap = new HashMap<>();
        for (String executorKey : section.getKeys(false)) {
            if (section.contains(executorKey + ".template") && section.isString(executorKey + ".template")) {
                ConfigurationSection templateSection = Main.getInstance().getTemplateConfig().getConfigurationSection("executors." + section.getString(executorKey + ".template"));
                if ((!templateSection.contains("type") && !templateSection.isString("type"))
                        || (!section.contains(executorKey + ".queue") && !section.isInt(executorKey + ".queue")))
                    continue;
                executorMap.put(executorKey, new Executor(executorKey,
                        section.getInt(executorKey + ".queue"),
                        rank,
                        templateSection.getConfigurationSection("data"),
                        ExecutorType.valueOf(templateSection.getString("type").toUpperCase())));
            } else {
                if ((!section.contains(executorKey + ".type") && !section.isString(executorKey + ".type"))
                        || (!section.contains(executorKey + ".queue") && !section.isInt(executorKey + ".queue")))
                    continue;
                executorMap.put(executorKey, new Executor(executorKey,
                        section.getInt(executorKey + ".queue"),
                        rank,
                        section.getConfigurationSection(executorKey + ".data"),
                        ExecutorType.valueOf(section.getString(executorKey + ".type").toUpperCase())));
            }
        }
        return executorMap;
    }

    public static void runAllExecutors(Player player, Rank rank) {
        for (Executor executor : rank.getSortedExecutors()) {
            executor.run(player);
        }
    }

}
