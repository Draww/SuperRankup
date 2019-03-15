package me.draww.superrup.condition;

import me.draww.superrup.Rank;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Condition {

    private final String id;
    private final Integer queue;
    private final Rank rank;
    private ConfigurationSection requiredDataSection;
    private String message;
    private ConditionType type;

    public Condition(String id, Integer queue, Rank rank, ConfigurationSection requiredDataSection, String message, ConditionType type) {
        this.id = id;
        this.queue = queue;
        this.rank = rank;
        this.requiredDataSection = requiredDataSection;
        this.message = message;
        this.type = type;
    }

    public boolean test(Player player) {
        return type.getPredicate().test(player, this);
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

    public ConfigurationSection getRequiredDataSection() {
        return requiredDataSection;
    }

    public void setRequiredDataSection(ConfigurationSection requiredDataSection) {
        this.requiredDataSection = requiredDataSection;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConditionType getType() {
        return type;
    }

    public void setType(ConditionType type) {
        this.type = type;
    }
}
