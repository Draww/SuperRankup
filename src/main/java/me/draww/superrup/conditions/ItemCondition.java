package me.draww.superrup.conditions;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Condition;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.utils.ActionUtil;
import me.draww.superrup.utils.InventoryUtil;
import me.draww.superrup.utils.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class ItemCondition implements Condition<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "message", required = true, custom = true)
    private String message;

    @ActionField(type = "item", required = true, custom = true)
    private ItemStack item;

    @Override
    public boolean test(Player player) {
        if (!InventoryUtil.hasItems(new ItemStack[]{ItemUtil.redesignPlaceholderItemStack(player, item)}, player.getInventory())) {
            ActionUtil.message(player, message, this);
            return false;
        }
        return true;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Integer queue() {
        return queue;
    }

    @Override
    public Rank rank() {
        return rank;
    }
}
