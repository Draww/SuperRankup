package me.draww.superrup.condition;

import me.draww.superrup.Rank;
import org.bukkit.entity.Player;

import java.util.Map;

public class Condition {

    private final String id;
    private final Rank rank;
    private Map<String, Object> requiredData;
    private String message;
    private ConditionType type;

    public Condition(String id, Rank rank, Map<String, Object> requiredData, String message, ConditionType type) {
        this.id = id;
        this.rank = rank;
        this.requiredData = requiredData;
        this.message = message;
        this.type = type;
    }

    public boolean test(Player player) {
        return type.getPredicate().test(player, this);
    }

    public String getId() {
        return id;
    }

    public Rank getRank() {
        return rank;
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
