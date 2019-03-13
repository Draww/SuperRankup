package me.draww.superrup;

import me.draww.superrup.group.IGroupManager;
import me.draww.superrup.group.LuckPermsGroupManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    private Config config;
    private Config ranksConfig;

    private IGroupManager groupManager;
    private RankManager rankManager;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config(this, "config.yml", true);
        ranksConfig = new Config(this, "ranks.yml", true);
        initGroupManager();
        rankManager = new RankManager(this);
        rankManager.init();
    }

    private void initGroupManager() {
        if (config.getConfig().getString("permission_provider").equals("LuckPerms")) {
            groupManager = new LuckPermsGroupManager();
        }
    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return instance;
    }

    public Config getMainConfig() {
        return config;
    }

    public Config getRanksConfig() {
        return ranksConfig;
    }

    public IGroupManager getGroupManager() {
        return groupManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }
}
