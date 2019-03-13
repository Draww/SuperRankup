package me.draww.superrup.executor;

import me.draww.superrup.Rank;
import me.draww.superrup.utils.TriConsumer;
import org.bukkit.entity.Player;

public enum ExecutorType {;

    private TriConsumer<Player, Executor, Rank> consumer;

    ExecutorType(TriConsumer<Player, Executor, Rank> consumer) {
        this.consumer = consumer;
    }

    public TriConsumer<Player, Executor, Rank> getConsumer() {
        return consumer;
    }
}
