package me.draww.superrup;

import me.blackness.black.Blackness;
import me.draww.superrup.api.SuperRankupAPI;
import me.draww.superrup.group.IGroupManager;
import me.draww.superrup.group.LuckPermsGroupManager;
import me.draww.superrup.group.PermissionsExGroupManager;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("WeakerAccess")
public class Main extends JavaPlugin {

    public static Main INSTANCE;

    private SuperRankupAPI api;

    private Config config;
    private Config ranksConfig;
    private Config templateConfig;
    private Config languageConfig;

    private IGroupManager groupManager;
    private RankManager rankManager;
    private Economy vaultEconomy;

    private Metrics metrics;

    @Override
    public void onEnable() {
        INSTANCE = this;
        config = new Config(this, "config.yml", true);
        ranksConfig = new Config(this, "ranks.yml", true);
        templateConfig = new Config(this, "template.yml", true);
        languageConfig = new Config(this, "language.yml", true);
        if (!setupGroupManager()) {
            getLogger().severe("group manager was not loaded.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupVaultManager()) {
            getLogger().severe("Vault was not loaded.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        api = new SuperRankupAPI();
        rankManager = new RankManager(this);
        rankManager.setup();
        getCommand("rank").setExecutor(new RankCommand());
        new Blackness().prepareFor(this);
        metrics = new Metrics(this);
    }

    public boolean controlPlaceholderAPI() {
        return this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    private boolean setupVaultManager() {
        if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> serviceProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
            if (serviceProvider != null) {
                this.vaultEconomy = serviceProvider.getProvider();
                return vaultEconomy != null;
            }
        }
        return false;
    }

    private boolean setupGroupManager() {
        String permissionProvider = config.getConfig().getString("permission_provider");
        if (permissionProvider.equals("LuckPerms") && this.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            groupManager = new LuckPermsGroupManager();
            return true;
        } else if (permissionProvider.equals("PermissionsEx") && this.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
            groupManager = new PermissionsExGroupManager();
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {

    }

    public void reload() {
        config.load();
        ranksConfig.load();
        templateConfig.load();
        languageConfig.load();
        api.getConditionRegisterer().reload();
        api.getExecutorRegisterer().reload();
        rankManager.reload();
    }

    public SuperRankupAPI getApi() {
        return api;
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

    public Config getLanguageConfig() {
        return languageConfig;
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

    public Metrics getMetrics() {
        return metrics;
    }
}
