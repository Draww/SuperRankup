package me.draww.superrup.executor;

import me.draww.superrup.Rank;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutorProvider {

    public static Map<String, Executor> deserializeExecutors(ConfigurationSection section) {
        Map<String, Executor> executorMap = new HashMap<>();
        for (String executorKey : section.getKeys(false)) {
            if ((!section.contains(executorKey + ".type") && ! section.isString(executorKey + ".type"))
                    || (!section.contains(executorKey + ".message") && ! section.isString(executorKey + ".message"))) continue;
            executorMap.put(executorKey, new Executor(executorKey,
                    section.getConfigurationSection(executorKey + ".data").getValues(false),
                    ExecutorType.valueOf(section.getString(executorKey + ".type").toUpperCase())));
        }
        return executorMap;
    }

    public static void runAllExecutors(Player player, Rank rank, List<Executor> executors) {
        for (Executor executor : executors) {
            executor.run(player, rank);
        }
    }

}
