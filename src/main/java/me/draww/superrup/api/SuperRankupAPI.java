package me.draww.superrup.api;

public class SuperRankupAPI {

    private ConditionRegisterer conditionRegisterer;
    private ExecutorRegisterer executorRegisterer;

    public SuperRankupAPI() {
        this.conditionRegisterer = new ConditionRegisterer();
        this.executorRegisterer = new ExecutorRegisterer();
    }

    public void registerCondition(String id, Class clazz) {
        this.conditionRegisterer.register(id, clazz);
    }

    public void registerExecutor(String id, Class clazz) {
        this.executorRegisterer.register(id, clazz);
    }

    public ConditionRegisterer getConditionRegisterer() {
        return conditionRegisterer;
    }

    public ExecutorRegisterer getExecutorRegisterer() {
        return executorRegisterer;
    }
}
