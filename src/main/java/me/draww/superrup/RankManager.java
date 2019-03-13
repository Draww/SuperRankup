package me.draww.superrup;

import me.draww.superrup.condition.ConditionProvider;
import me.draww.superrup.executor.ExecutorProvider;
import me.draww.superrup.utils.ItemUtil;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class RankManager {

    private final Main plugin;
    private final Config ranksConfig;

    private Map<String, Rank> rankMap = new HashMap<>();

    public RankManager(Main plugin) {
        this.plugin = plugin;
        this.ranksConfig = plugin.getRanksConfig();
    }

    public Rank get(String id) {
        if (!has(id)) return null;
        return rankMap.get(id);
    }

    public void add(String id, Rank rank) {
        if (!has(id)) {
            rankMap.put(id, rank);
        }
    }

    public void remove(String id) {
        if (has(id)) {
            rankMap.remove(id);
        }
    }

    public boolean has(String id) {
        return rankMap.containsKey(id);
    }

    public boolean isEmpty() {
        return rankMap.isEmpty();
    }

    public Optional<Rank> getRankFromID(String id){
        return Optional.ofNullable(get(id));
    }

    public List<Rank> getSortedRanks(){
        if (!getRankMap().isEmpty()){
            return new ArrayList<>(getRankMap().values()).stream().sorted(Comparator.comparing(Rank::getQueue)).collect(Collectors.toList());
        }
        return null;
    }

    public void init() {
        for (String rank : ranksConfig.getConfig().getKeys(false)) {
            if ((!ranksConfig.getConfig().contains(rank + ".group") && !ranksConfig.getConfig().isString(rank + ".group"))
                    || (!ranksConfig.getConfig().contains(rank + ".queue") && !ranksConfig.getConfig().isInt(rank + ".queue"))
                    || (!ranksConfig.getConfig().contains(rank + ".icon") && !ranksConfig.getConfig().isConfigurationSection(rank + ".icon"))
                    || (!ranksConfig.getConfig().contains(rank + ".conditions")) && !ranksConfig.getConfig().isConfigurationSection(rank + ".conditions")) continue;
            rankMap.put(rank, new Rank(rank,
                    ranksConfig.getConfig().getString(rank + ".group"),
                    ranksConfig.getConfig().getInt(rank + ".queue"),
                    ItemUtil.deserializeItemStack(ranksConfig.getConfig().getConfigurationSection(rank + ".icon.low")),
                    ItemUtil.deserializeItemStack(ranksConfig.getConfig().getConfigurationSection(rank + ".icon.jump")),
                    ItemUtil.deserializeItemStack(ranksConfig.getConfig().getConfigurationSection(rank + ".icon.equal")),
                    ItemUtil.deserializeItemStack(ranksConfig.getConfig().getConfigurationSection(rank + ".icon.high")),
                    ConditionProvider.deserializeConditions(ranksConfig.getConfig().getConfigurationSection(rank + ".conditions")),
                    ExecutorProvider.deserializeExecutors(ranksConfig.getConfig().getConfigurationSection(rank + ".executors"))));
            Main.getInstance().getLogger().info(rank + " Loaded!");
        }
    }

    public Main getPlugin() {
        return plugin;
    }

    public Config getRanksConfig() {
        return ranksConfig;
    }

    public Map<String, Rank> getRankMap() {
        return rankMap;
    }
}
