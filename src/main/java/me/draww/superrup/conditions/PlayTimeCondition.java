package me.draww.superrup.conditions;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Condition;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.utils.ActionUtil;
import me.draww.superrup.utils.TimeUtil;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

@SuppressWarnings({"unused"})
public class PlayTimeCondition implements Condition<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "message", required = true, custom = true, replaceVariables = true)
    private String message;

    @ActionField(type = "time", required = true, custom = true)
    private Integer time;

    @Override
    public boolean test(Player player) {
        long onlineTimeSecond = player.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
        if (onlineTimeSecond < time) {
            long betweenTimeSecond = time - onlineTimeSecond;
            String requiredTime = TimeUtil.to(betweenTimeSecond);
            String currentOnlineTime = TimeUtil.to(onlineTimeSecond);
            String replacedMessage = message.replace("%reqTime%", requiredTime)
                    .replace("%onlineTime%", currentOnlineTime);
            ActionUtil.message(player, replacedMessage, this);
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
