package me.draww.superrup.executors;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class SoundExecutor implements Executor<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "value", required = true, custom = true)
    private String sound;

    @Override
    public void run(Player player) {
        final float soundFloat = 1.0f;
        player.playSound(player.getLocation(), Sound.valueOf(sound.toUpperCase()), soundFloat, soundFloat);
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
