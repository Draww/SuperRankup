package me.draww.superrup.group;

import org.bukkit.entity.Player;

import java.util.List;

public interface IGroupManager {

    boolean inGroup(Player player, String group);

    List<String> getPlayerGroups(Player player);

    String getPlayerPrimaryGroup(Player player);

    default String getPlayerPrimaryGroup(Player player, String def) {
        String primaryGroup = getPlayerPrimaryGroup(player);
        return primaryGroup == null ? def : primaryGroup;
    }

    void setPlayerPrimaryGroup(Player player, String group);

    void reload();

    void close();

}
