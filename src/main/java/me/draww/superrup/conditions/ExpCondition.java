package me.draww.superrup.conditions;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Condition;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.utils.ActionUtil;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class ExpCondition implements Condition<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "message", required = true, custom = true, replaceVariables = true)
    private String message;

    @ActionField(type = "value", required = true, custom = true)
    private Double exp;

    @Override
    public boolean test(Player player) {
        if (player.getExp() != exp) {
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
