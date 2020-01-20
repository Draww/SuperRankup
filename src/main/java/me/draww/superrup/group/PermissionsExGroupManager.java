package me.draww.superrup.group;

import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Arrays;
import java.util.List;

public class PermissionsExGroupManager implements IGroupManager {

    private PermissionUser loadUser(Player player) {
        // assert that the player is online
        if (!player.isOnline()) {
            throw new IllegalStateException("Player is offline!");
        }
        return PermissionsEx.getUser(player);
    }

    @Override
    public boolean inGroup(Player player, String group) {
        PermissionUser pexUser;
        try {
            pexUser = loadUser(player);
        } catch (IllegalStateException ex) { return false; }
        return pexUser.inGroup(group);
    }

    @Override
    public List<String> getPlayerGroups(Player player) {
        PermissionUser pexUser;
        try {
            pexUser = loadUser(player);
        } catch (IllegalStateException ex) { return null; }
        return Arrays.asList(pexUser.getGroupNames());
    }

    @Override
    public String getPlayerPrimaryGroup(Player player) {
        PermissionUser pexUser;
        try {
            pexUser = loadUser(player);
        } catch (IllegalStateException ex) { return null; }
        return pexUser.getGroupNames()[0];
    }

    @Override
    public void setPlayerPrimaryGroup(Player player, String group) {
        PermissionUser pexUser;
        try {
            pexUser = loadUser(player);
        } catch (IllegalStateException ex) { return; }
        pexUser.setGroups(new String[]{group});
    }

    @Override
    public void reload() {

    }

    @Override
    public void close() {

    }
}
