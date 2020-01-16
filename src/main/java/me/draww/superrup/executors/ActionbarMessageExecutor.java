package me.draww.superrup.executors;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.utils.StringUtil;
import me.draww.superrup.utils.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class ActionbarMessageExecutor implements Executor<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "message", required = true, custom = true)
    private String message;

    @Override
    public boolean onSetup() {
        message = message
                .replace("%rank%", rank.getId())
                .replace("%rank_group%", rank.getGroup());
        return true;
    }

    @Override
    public void run(Player player) {
        String editedMessage = StringUtil.replacePlayerPlaceholders(player, this.message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Text.colorize(editedMessage)));
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
