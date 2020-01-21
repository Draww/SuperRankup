package me.draww.superrup.api;

import me.draww.superrup.Main;
import me.draww.superrup.Settings;

public class SuperRankupAPI {

    public static SuperRankupAPI INSTANCE;

    private Main plugin;
    private ConditionRegisterer conditionRegisterer;
    private ExecutorRegisterer executorRegisterer;

    public SuperRankupAPI(Main plugin) {
        INSTANCE = this;
        this.plugin = plugin;
        this.conditionRegisterer = new ConditionRegisterer();
        this.executorRegisterer = new ExecutorRegisterer();
    }

    public void registerCondition(String id, Class clazz) {
        this.conditionRegisterer.register(id, clazz);
    }

    public void registerExecutor(String id, Class clazz) {
        this.executorRegisterer.register(id, clazz);
    }

    public Main getPlugin() {
        return plugin;
    }

    public ConditionRegisterer getConditionRegisterer() {
        return conditionRegisterer;
    }

    public ExecutorRegisterer getExecutorRegisterer() {
        return executorRegisterer;
    }

    public Settings getSettings() {
        return plugin.getSettings();
    }
}
