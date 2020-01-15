package me.draww.superrup.executors;

import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class TeleportExecutor implements Executor<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "value", required = true, custom = true)
    private String value;

    @Override
    public void run(Player player) {
        final String[] location = value.split(";");
        Location destination;
        if (location.length == 4){
            final World world = Bukkit.getWorld(location[0]);
            final double x = Double.parseDouble(location[1]);
            final double y = Double.parseDouble(location[2]);
            final double z = Double.parseDouble(location[3]);
            destination = new Location(world, x, y, z);
        } else if (location.length == 6){
            final World world = Bukkit.getWorld(location[0]);
            final double x = Double.parseDouble(location[1]);
            final double y = Double.parseDouble(location[2]);
            final double z = Double.parseDouble(location[3]);
            final float yaw = Float.parseFloat(location[4]);
            final float pitch = Float.parseFloat(location[5]);
            destination = new Location(world, x, y, z, yaw, pitch);
        } else {
            destination = null;
        }
        if (destination != null) player.teleport(destination);
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
