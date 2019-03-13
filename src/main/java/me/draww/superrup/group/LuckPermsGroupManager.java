package me.draww.superrup.group;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LuckPermsGroupManager implements IGroupManager {

    private LuckPermsApi api;

    public LuckPermsGroupManager() {
        api = LuckPerms.getApi();
    }

    private User loadUser(Player player) {
        // assert that the player is online
        if (!player.isOnline()) {
            throw new IllegalStateException("Player is offline!");
        }
        return api.getUserManager().getUser(player.getUniqueId());
    }

    @Override
    public boolean inGroup(Player player, String group) {
        if (!player.isOnline()) throw new IllegalStateException("Player is offline!");
        return player.hasPermission("group." + group);
    }

    @Override
    public List<String> getPlayerGroups(Player player) {
        User luckUser;
        try {
            luckUser = loadUser(player);
        } catch (IllegalStateException ex) { return null; }
        List<String> groups = new ArrayList<>();
        for (Node node : luckUser.getOwnNodes()) {
            if (node.isGroupNode()) {
                groups.add(node.getGroupName());
            }
        }
        return groups;
    }

    @Override
    public String getPlayerPrimaryGroup(Player player) {
        User luckUser;
        try {
            luckUser = loadUser(player);
        } catch (IllegalStateException ex) { return null; }
        return luckUser.getPrimaryGroup();
    }

    @Override
    public void setPlayerPrimaryGroup(Player player, String group) {
        User luckUser;
        try {
            luckUser = loadUser(player);
        } catch (IllegalStateException ex) { return; }
        luckUser.setPermission(api.getNodeFactory().makeGroupNode(Objects.requireNonNull(api.getGroup(group))).build());
        luckUser.setPrimaryGroup(group);
        api.getUserManager().saveUser(luckUser);
    }
}
