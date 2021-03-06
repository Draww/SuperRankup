package me.draww.superrup;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

@SuppressWarnings("WeakerAccess")
public class Config {

    private File configFile;
    private String filename;
    private JavaPlugin plugin;
    private boolean shouldCopy;
    private FileConfiguration fileConfiguration;

    public Config(JavaPlugin plugin, String filename, Boolean shouldCopy){
        this.filename = filename;
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), this.filename);
        this.shouldCopy = shouldCopy;
        if (shouldCopy){
            this.firstRun(plugin);
        } else {
            if (!configFile.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public FileConfiguration getConfig() {
        return this.fileConfiguration;
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            this.fileConfiguration.load(this.configFile);
        } catch (FileNotFoundException e3) {
            this.configFile = new File(this.plugin.getDataFolder(), this.filename);
            if (this.shouldCopy) {
                this.firstRun(this.plugin);
            }
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    private void firstRun(JavaPlugin plugin) {
        if (!this.configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            this.configFile.getParentFile().mkdirs();
            copy(plugin.getResource(this.filename), this.configFile);
        }
    }

    public static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[63];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        catch (Exception ignored) {}
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.getConfig().getConfigurationSection(path);
    }



}