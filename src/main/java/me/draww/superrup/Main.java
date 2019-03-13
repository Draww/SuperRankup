package me.draww.superrup;

import me.draww.superrup.group.IGroupManager;
import me.draww.superrup.group.LuckPermsGroupManager;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    private Config config;
    private Config ranksConfig;
    private Config templateConfig;

    private IGroupManager groupManager;
    private RankManager rankManager;
    private Economy vaultEconomy;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config(this, "config.yml", true);
        ranksConfig = new Config(this, "ranks.yml", true);
        templateConfig = new Config(this, "template.yml", true);
        if (!initGroupManager()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!initVaultManager()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        rankManager = new RankManager(this);
        rankManager.init();
    }

    private boolean initVaultManager() {
        if (this.getServer().getPluginManager().getPlugin("Vault") instanceof Vault) {
            RegisteredServiceProvider<Economy> serviceProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
            if (serviceProvider != null) {
                this.vaultEconomy = serviceProvider.getProvider();
                return vaultEconomy != null;
            }
        }
        return false;
    }

    private boolean initGroupManager() {
        if (config.getConfig().getString("permission_provider").equals("LuckPerms") && this.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            groupManager = new LuckPermsGroupManager();
            return true;
        }
        return false;
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

    public Config getTemplateConfig() {
        return templateConfig;
    }

    public IGroupManager getGroupManager() {
        return groupManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public Economy getVaultEconomy() {
        return vaultEconomy;
    }
}
