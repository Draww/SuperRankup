package me.draww.superrup;

import me.draww.superrup.condition.ConditionProvider;
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
        ConfigurationSection section = ranksConfig.getConfig().getDefaultSection();
        for (String rank : section.getKeys(false)) {
            if ((!section.contains(rank + ".group") && !section.isString(rank + ".group"))
                    || (!section.contains(rank + ".queue") && !section.isInt(rank + ".queue"))
                    || (!section.contains(rank + ".icon") && !section.isConfigurationSection(rank + ".icon"))
                    || (!section.contains(rank + ".conditions")) && !section.isConfigurationSection(rank + ".conditions")) continue;
            rankMap.put(rank, new Rank(rank,
                    section.getString(rank + ".group"),
                    section.getInt(rank + ".queue"),
                    ItemUtil.deserializeItemStack(section.getConfigurationSection(rank + ".icon")),
                    ConditionProvider.deserializeConditions(section.getConfigurationSection(rank + ".conditions"))));
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
