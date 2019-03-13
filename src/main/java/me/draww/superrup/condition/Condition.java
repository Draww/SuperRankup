package me.draww.superrup.condition;

import me.draww.superrup.Rank;
import org.bukkit.entity.Player;

import java.util.Map;

public class Condition {

    private final String id;
    private Map<String, Object> requiredData;
    private String message;
    private ConditionType type;

    public Condition(String id, Map<String, Object> requiredData, ConditionType type) {
        this.id = id;
        this.requiredData = requiredData;
        this.type = type;
    }

    public boolean test(Player player, Rank rank) {
        return type.getPredicate().test(player, this, rank);
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getRequiredData() {
        return requiredData;
    }

    public void setRequiredData(Map<String, Object> requiredData) {
        this.requiredData = requiredData;
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
