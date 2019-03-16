package me.draww.superrup.executor;

import me.draww.superrup.Main;
import me.draww.superrup.utils.InventoryUtil;
import me.draww.superrup.utils.ItemUtil;
import me.draww.superrup.utils.StringUtil;
import me.draww.superrup.utils.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.BiConsumer;

public enum ExecutorType {
    MESSAGE((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isString("value")) return;
        String data = executor.getDataSection().getString("value");
        data = StringUtil.replacePlayerPlaceholders(player, data
                .replace("%rank%", executor.getRank().getId())
                .replace("%rank_group%", executor.getRank().getGroup()));
        player.sendMessage(Text.colorize(data));
    }),
    ACTIONBAR_MESSAGE((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isString("value")) return;
        String data = executor.getDataSection().getString("value");
        data = StringUtil.replacePlayerPlaceholders(player, data
                .replace("%rank%", executor.getRank().getId())
                .replace("%rank_group%", executor.getRank().getGroup()));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Text.colorize(data)));
    }),
    BROADCAST_MESSAGE((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isString("value")) return;
        String data = executor.getDataSection().getString("value");
        data = StringUtil.replacePlayerPlaceholders(player, data
                .replace("%rank%", executor.getRank().getId())
                .replace("%rank_group%", executor.getRank().getGroup()));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Text.colorize(StringUtil.replacePlayerPlaceholders(p, data.replace("%loopPlayer", "%player"))));
        }
    }),
    BROADCAST_ACTIONBAR_MESSAGE((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isString("value")) return;
        String data = executor.getDataSection().getString("value");
        data = StringUtil.replacePlayerPlaceholders(player, data
                .replace("%rank%", executor.getRank().getId())
                .replace("%rank_group%", executor.getRank().getGroup()));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Text.colorize(StringUtil.replacePlayerPlaceholders(p, data.replace("%loopPlayer", "%player")))));
        }
    }),
    WITHDRAW_MONEY((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isDouble("value")) return;
        double data = executor.getDataSection().getDouble("value");
        Main.getInstance().getVaultEconomy().withdrawPlayer(player, data);
    }),
    DEPOSIT_MONEY((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isDouble("value")) return;
        double data = executor.getDataSection().getDouble("value");
        Main.getInstance().getVaultEconomy().depositPlayer(player, data);
    }),
    CONSOLE_COMMAND((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isString("value")) return;
        String data = executor.getDataSection().getString("value");
        data = StringUtil.replacePlayerPlaceholders(player, data
                .replace("%rank%", executor.getRank().getId())
                .replace("%rank_group%", executor.getRank().getGroup()));
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), data);
    }),
    PLAYER_COMMAND((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isString("value")) return;
        String data = executor.getDataSection().getString("value");
        data = StringUtil.replacePlayerPlaceholders(player, data
                .replace("%rank%", executor.getRank().getId())
                .replace("%rank_group%", executor.getRank().getGroup()));
        player.performCommand(data);
    }),
    SOUND((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isString("value")) return;
        String data = executor.getDataSection().getString("value");
        final float soundFloat = 1.0f;
        player.playSound(player.getLocation(), Sound.valueOf(data.toUpperCase()), soundFloat, soundFloat);
    }),
    TELEPORT((player, executor) -> {
        if (!executor.getDataSection().contains("value") || !executor.getDataSection().isString("value")) return;
        String data = executor.getDataSection().getString("value");
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
    }),
    GIVE_ITEM((player, executor) -> {
        if (!executor.getDataSection().contains("item") || !executor.getDataSection().isConfigurationSection("item")) return;
        ItemStack serializeItem;
        try {
            serializeItem = ItemUtil.redesignPlaceholderItemStack(player, Objects.requireNonNull(ItemUtil.deserializeItemStack(executor.getDataSection().getConfigurationSection("item"), executor.getRank())));
        } catch (NullPointerException e) {
            return;
        }
        InventoryUtil.add(serializeItem, player.getInventory());
    }),
    REMOVE_ITEM((player, executor) -> {
        if (!executor.getDataSection().contains("item") || !executor.getDataSection().isConfigurationSection("item")) return;
        ItemStack serializeItem;
        try {
            serializeItem = ItemUtil.redesignPlaceholderItemStack(player, Objects.requireNonNull(ItemUtil.deserializeItemStack(executor.getDataSection().getConfigurationSection("item"), executor.getRank())));
        } catch (NullPointerException e) {
            return;
        }
        InventoryUtil.remove(serializeItem, player.getInventory());
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
