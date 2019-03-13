package me.draww.superrup.executor;

import me.draww.superrup.Rank;
import org.bukkit.entity.Player;

import java.util.Map;

public class Executor {

    private final String id;
    private Map<String, Object> data;
    private ExecutorType type;

    public Executor(String id, Map<String, Object> data, ExecutorType type) {
        this.id = id;
        this.data = data;
        this.type = type;
    }

    public void run(Player player, Rank rank) {
        type.getConsumer().accept(player, this, rank);
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public ExecutorType getType() {
        return type;
    }

    public void setType(ExecutorType type) {
        this.type = type;
    }

}
