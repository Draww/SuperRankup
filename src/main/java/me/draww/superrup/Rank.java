package me.draww.superrup;

import me.draww.superrup.api.Condition;
import me.draww.superrup.api.Executor;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class Rank {

    private String id;
    private String group;
    private Integer queue;

    private ItemStack iconLow;
    private ItemStack iconJump;
    private ItemStack iconEqual;
    private ItemStack iconHigh;

    private Map<String, Condition> conditions;
    private Map<String, Executor> executors;

    public Rank(String id, String group, Integer queue, ItemStack iconLow, ItemStack iconJump, ItemStack iconEqual, ItemStack iconHigh, Map<String, Condition> conditions, Map<String, Executor> executors) {
        this.id = id;
        this.group = group;
        this.queue = queue;
        this.iconLow = iconLow;
        this.iconJump = iconJump;
        this.iconEqual = iconEqual;
        this.iconHigh = iconHigh;
        this.conditions = conditions;
        this.executors = executors;
    }

    public Rank(String id, String group, Integer queue) {
        this.id = id;
        this.group = group;
        this.queue = queue;
    }

    public List<Condition> getSortedConditions() {
        return conditions.values().stream().sorted(Comparator.comparingInt(Condition::queue)).collect(Collectors.toList());
    }

    public List<Executor> getSortedExecutors() {
        return executors.values().stream().sorted(Comparator.comparingInt(Executor::queue)).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getQueue() {
        return queue;
    }

    public void setQueue(Integer queue) {
        this.queue = queue;
    }

    public ItemStack getIconLow() {
        return iconLow;
    }

    public void setIconLow(ItemStack iconLow) {
        this.iconLow = iconLow;
    }

    public ItemStack getIconJump() {
        return iconJump;
    }

    public void setIconJump(ItemStack iconJump) {
        this.iconJump = iconJump;
    }

    public ItemStack getIconEqual() {
        return iconEqual;
    }

    public void setIconEqual(ItemStack iconEqual) {
        this.iconEqual = iconEqual;
    }

    public ItemStack getIconHigh() {
        return iconHigh;
    }

    public void setIconHigh(ItemStack iconHigh) {
        this.iconHigh = iconHigh;
    }

    public Map<String, Condition> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Condition> conditions) {
        this.conditions = conditions;
    }

    public Map<String, Executor> getExecutors() {
        return executors;
    }

    public void setExecutors(Map<String, Executor> executors) {
        this.executors = executors;
    }
}
