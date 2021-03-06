package me.draww.superrup.executors;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.api.exception.ActionException;
import me.draww.superrup.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class ConsoleCommandExecutor implements Executor<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "value", required = true, custom = true, replaceVariables = true)
    private String command;

    @Override
    public void onSetup() throws ActionException {
        command = command
                .replace("%rank%", rank.getId())
                .replace("%rank_group%", rank.getGroup());
    }

    @Override
    public void run(Player player) {
        String editedCommand = Text.replacePlayerPlaceholders(player, this.command);
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), editedCommand);
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
