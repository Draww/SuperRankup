package me.draww.superrup.executors;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.api.exception.ActionException;
import me.draww.superrup.utils.Text;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class MessageExecutor implements Executor<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "value", required = true, custom = true, replaceVariables = true)
    private String message;

    @Override
    public void onSetup() throws ActionException {
        message = message
                .replace("%rank%", rank.getId())
                .replace("%rank_group%", rank.getGroup());
    }

    @Override
    public void run(Player player) {
        String editedMessage = Text.replacePlayerPlaceholders(player, this.message);
        player.sendMessage(Text.colorize(editedMessage));
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
