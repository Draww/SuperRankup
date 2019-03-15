package me.draww.superrup.executor;

import me.draww.superrup.Rank;
import org.bukkit.entity.Player;

import java.util.Map;

public class Executor {

    private final String id;
    private final Integer queue;
    private final Rank rank;
    private Map<String, Object> data;
    private ExecutorType type;

    public Executor(String id, Integer queue, Rank rank, Map<String, Object> data, ExecutorType type) {
        this.id = id;
        this.queue = queue;
        this.rank = rank;
        this.data = data;
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
