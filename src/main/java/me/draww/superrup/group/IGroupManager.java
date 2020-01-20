package me.draww.superrup.group;

import org.bukkit.entity.Player;

import java.util.List;

public interface IGroupManager {

    boolean inGroup(Player player, String group);

    List<String> getPlayerGroups(Player player);

    String getPlayerPrimaryGroup(Player player);

    void setPlayerPrimaryGroup(Player player, String group);

    void reload();

    void close();

}
