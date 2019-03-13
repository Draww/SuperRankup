package me.draww.superrup;

import me.draww.superrup.condition.Condition;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Rank {

    private String id;
    private String group;
    private Integer queue;

    private ItemStack icon;

    private Map<String, Condition> conditions;

    public Rank(String id, String group, Integer queue, ItemStack icon, Map<String, Condition> conditions) {
        this.id = id;
        this.group = group;
        this.queue = queue;
        this.icon = icon;
        this.conditions = conditions;
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

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public Map<String, Condition> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Condition> conditions) {
        this.conditions = conditions;
    }
}
