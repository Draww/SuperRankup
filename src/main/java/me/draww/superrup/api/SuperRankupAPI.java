package me.draww.superrup.api;

import me.draww.superrup.Main;

public class SuperRankupAPI {

    private Main plugin;
    private ConditionRegisterer conditionRegisterer;
    private ExecutorRegisterer executorRegisterer;

    public SuperRankupAPI(Main plugin) {
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
}
