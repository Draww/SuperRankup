package me.draww.superrup.executor;

import me.draww.superrup.Main;
import me.draww.superrup.utils.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public enum ExecutorType {
    MESSAGE((player, executor) -> {
        String data = (String) executor.getData().get("value");
        data = data
                .replace("{rank}", executor.getRank().getId())
                .replace("{player}", player.getName());
        player.sendMessage(Text.colorize(data));
    }),
    ACTIONBAR_MESSAGE((player, executor) -> {
        String data = (String) executor.getData().get("value");
        data = data
                .replace("{rank}", executor.getRank().getId())
                .replace("{player}", player.getName());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Text.colorize(data)));
    }),
    BROADCAST_MESSAGE((player, executor) -> {
        String data = (String) executor.getData().get("value");
        data = data
                .replace("{rank}", executor.getRank().getId())
                .replace("{player}", player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Text.colorize(data.replace("{loopPlayer}", p.getName())));
        }
    }),
    BROADCAST_ACTIONBAR_MESSAGE((player, executor) -> {
        String data = (String) executor.getData().get("value");
        data = data
                .replace("{rank}", executor.getRank().getId())
                .replace("{player}", player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Text.colorize(data.replace("{loopPlayer}", p.getName()))));
        }
    }),
    WITHDRAW_MONEY((player, executor) -> {
        double data;
        try {
            data = (double) executor.getData().get("value");
        } catch (Exception e) {
            return;
        }
        Main.getInstance().getVaultEconomy().withdrawPlayer(player, data);
    }),
    DEPOSIT_MONEY((player, executor) -> {
        double data;
        try {
            data = (double) executor.getData().get("value");
        } catch (Exception e) {
            return;
        }
        Main.getInstance().getVaultEconomy().depositPlayer(player, data);
    }),
    CONSOLE_COMMAND((player, executor) -> {
        String data = (String) executor.getData().get("value");
        data = data
                .replace("{rank}", executor.getRank().getId())
                .replace("{player}", player.getName());
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), data);
    }),
    PLAYER_COMMAND((player, executor) -> {
        String data = (String) executor.getData().get("value");
        data = data
                .replace("{rank}", executor.getRank().getId())
                .replace("{player}", player.getName());
        player.performCommand(data);
    }),
    SOUND((player, executor) -> {
        String data = (String) executor.getData().get("value");
        final float soundFloat = 1.0f;
        player.playSound(player.getLocation(), Sound.valueOf(data.toUpperCase()), soundFloat, soundFloat);
    }),
    TELEPORT((player, executor) -> {
        String data = (String) executor.getData().get("value");
        final String[] location = data.split(";");
        Location destination = null;
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
        }
        if (location != null) {
            player.teleport(destination);
        }
    })


    ;

    private BiConsumer<Player, Executor> consumer;

    ExecutorType(BiConsumer<Player, Executor> consumer) {
        this.consumer = consumer;
    }

    public BiConsumer<Player, Executor> getConsumer() {
        return consumer;
    }
}
