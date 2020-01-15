package me.draww.superrup;

import me.blackness.black.Blackness;
import me.draww.superrup.group.IGroupManager;
import me.draww.superrup.group.LuckPermsGroupManager;
import me.draww.superrup.group.PermissionsExGroupManager;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("WeakerAccess")
public class Main extends JavaPlugin {

    private static Main instance;

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
        instance = this;
        config = new Config(this, "config.yml", true);
        ranksConfig = new Config(this, "ranks.yml", true);
        templateConfig = new Config(this, "template.yml", true);
        languageConfig = new Config(this, "language.yml", true);
        if (!initGroupManager()) {
            getLogger().severe("group manager was not loaded.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!initVaultManager()) {
            getLogger().severe("Vault was not loaded.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        rankManager = new RankManager(this);
        rankManager.init();
        getCommand("rank").setExecutor(new RankCommand());
        new Blackness().prepareFor(this);
        metrics = new Metrics(this);
    }

    public boolean controlPlaceholderAPI() {
        return this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
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
        String permissionProvider = config.getConfig().getString("permission_provider");
        if (permissionProvider.equals("LuckPerms") && this.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            groupManager = new LuckPermsGroupManager();
            return true;
        } else if  (permissionProvider.equals("PermissionsEx") && this.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
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
        rankManager.reload();
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
