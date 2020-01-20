package me.draww.superrup.group;

import be.bendem.sqlstreams.SqlStreams;
import be.bendem.sqlstreams.UncheckedSqlException;
import be.bendem.sqlstreams.util.Wrap;
import me.draww.superrup.ChatListener;
import me.draww.superrup.Config;
import me.draww.superrup.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;

import java.io.File;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("FieldCanBeLocal")
public class SRGroupManager implements IGroupManager {

    private Type type;
    private Config groupsConfig;
    private Object data;

    private EventPriority chatListenerPriority;
    private ChatListener chatListener;

    public SRGroupManager(Type type) {
        this.type = type;
        try {
            loadManager();
        } catch (UncheckedSqlException err) {
            err.printStackTrace();
            Main.INSTANCE.getLogger().severe("Error establishing Database connection! and the Database model was changed to yaml.");
            Main.INSTANCE.getMainConfig().getConfig().set("settings.type", "YAML");
            Main.INSTANCE.getMainConfig().save();
            this.type = Type.YAML;
            loadManager();
        }
    }

    private void loadManager() throws UncheckedSqlException {
        groupsConfig = new Config(Main.INSTANCE, "groups.yml", true);
        chatListenerPriority = EventPriority.valueOf(Main.INSTANCE.getMainConfig().getConfig().getString("settings.listener_priority").toUpperCase());
        chatListener = new ChatListener(chatListenerPriority);
        chatListener.register();
        switch (type) {
            case YAML:
                data = new Config(Main.INSTANCE, "data.yml", false);
                break;
            case SQLite:
                data = Wrap.get(() -> SqlStreams.connect(DriverManager.getConnection("jdbc:sqlite:" + new File(Main.INSTANCE.getDataFolder(), "data.db").getAbsolutePath())));
                SqlStreams sqlStreams = getData();
                sqlStreams.exec("CREATE TABLE IF NOT EXISTS Players(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "playerName TEXT NOT NULL," +
                        "rank TEXT NOT NULL" +
                        ");");
                break;
        }
    }

    @Override
    public boolean inGroup(Player player, String group) {
        String playerGroup = getPlayerPrimaryGroup(player);
        if (playerGroup != null) return playerGroup.equals(group);
        else return false;
    }

    @Override
    public List<String> getPlayerGroups(Player player) {
        List<String> groups = new ArrayList<>();
        String playerGroup = getPlayerPrimaryGroup(player);
        if (playerGroup != null) groups.add(playerGroup);
        return groups;
    }

    @Override
    public String getPlayerPrimaryGroup(Player player) {
        switch (type) {
            case YAML:
                Config config = getData();
                if (config.getConfig().contains(player.getName())) {
                    return config.getConfig().getString(player.getName());
                } else {
                    return null;
                }
            case SQLite:
                SqlStreams sqlStreams = getData();
                Optional<String> optRank = sqlStreams.first("SELECT rank FROM Players WHERE playerName = ?", resultSet -> resultSet.getString("rank"), player.getName());
                return optRank.orElse(null);
            default:
                return null;
        }
    }

    @Override
    public void setPlayerPrimaryGroup(Player player, String group) {
        switch (type) {
            case YAML:
                Config config = getData();
                config.getConfig().set(player.getName(), group);
            case SQLite:
                SqlStreams sqlStreams = getData();
                Optional<String> optRank = sqlStreams.first("SELECT rank FROM Players WHERE playerName = ?", resultSet -> resultSet.getString("rank"), player.getName());
                if (optRank.isPresent()) {
                    sqlStreams.exec("UPDATE Players SET rank = ? WHERE playerName = ?", group, player.getName());
                } else {
                    sqlStreams.exec("INSERT INTO Players(playerName, rank) VALUES(?, ?)", player.getName(), group);
                }
        }
    }

    @Override
    public void close() {
        chatListener.unregister();
    }

    @SuppressWarnings("unchecked")
    private <T> T getData() {
        return (T) data;
    }

    public String getPrefix(Player player) {
        String group = getPlayerPrimaryGroup(player);
        if (group != null) return groupsConfig.getConfig().getString(group + ".prefix");
        else return "";
    }

    public enum Type {
        YAML, SQLite;
    }

}
