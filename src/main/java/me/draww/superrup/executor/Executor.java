package me.draww.superrup.executor;

import me.draww.superrup.Rank;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@SuppressWarnings("WeakerAccess")
public class Executor {

    private final String id;
    private final Integer queue;
    private final Rank rank;
    private ConfigurationSection dataSection;
    private ExecutorType type;

    public Executor(String id, Integer queue, Rank rank, ConfigurationSection dataSection, ExecutorType type) {
        this.id = id;
        this.queue = queue;
        this.rank = rank;
        this.dataSection = dataSection;
        this.type = type;
    }

    public void run(Player player) {
        type.getConsumer().accept(player, this);
    }

    public String getId() {
        return id;
    }

    public Integer getQueue() {
        return queue;
    }

    public Rank getRank() {
        return rank;
    }

    public ConfigurationSection getDataSection() {
        return dataSection;
    }

    public void setDataSection(ConfigurationSection dataSection) {
        this.dataSection = dataSection;
    }

    public ExecutorType getType() {
        return type;
    }

    public void setType(ExecutorType type) {
        this.type = type;
    }

}
