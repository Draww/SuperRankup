package me.draww.superrup.executors;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.api.exception.ActionException;
import me.draww.superrup.utils.StringUtil;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PlayerCommandExecutor implements Executor<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "value", required = true, custom = true)
    private String value;

    @Override
    public void onSetup() throws ActionException {
        value = value
                .replace("%rank%", rank.getId())
                .replace("%rank_group%", rank.getGroup());
    }

    @Override
    public void run(Player player) {
        String editedCommand = StringUtil.replacePlayerPlaceholders(player, this.value);
        player.performCommand(editedCommand);
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
