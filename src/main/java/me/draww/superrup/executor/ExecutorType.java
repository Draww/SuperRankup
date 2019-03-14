package me.draww.superrup.executor;

import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public enum ExecutorType {;

    private BiConsumer<Player, Executor> consumer;

    ExecutorType(BiConsumer<Player, Executor> consumer) {
        this.consumer = consumer;
    }

    public BiConsumer<Player, Executor> getConsumer() {
        return consumer;
    }
}
