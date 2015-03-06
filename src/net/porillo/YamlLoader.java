package net.porillo;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public abstract class YamlLoader {

    protected String fileName;
    protected File configFile;
    protected File dataFolder;
    protected ColoredGroups plugin;
    protected FileConfiguration config;

    protected YamlLoader(final ColoredGroups plugin, final String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.dataFolder = plugin.getDataFolder();
        this.configFile = new File(this.dataFolder, File.separator + fileName);
        config = loadConfiguration(this.configFile);
    }

    protected void load() {
        if (!this.configFile.exists()) {
            this.dataFolder.mkdir();
            saveConfig();
        }
        addDefaults();
        loadKeys();
        this.saveIfNotExist();
    }

    protected void saveConfig() {
        try {
            config.save(this.configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void saveIfNotExist() {
        if (!this.configFile.exists()) {
            if (this.plugin.getResource(this.fileName) != null) {
                this.plugin.saveResource(this.fileName, false);
            }
        }
        rereadFromDisk();
    }

    protected void rereadFromDisk() {
        config = loadConfiguration(this.configFile);
    }

    /**
     * Api method to create a new config section for a group
     *
     * @param a Group Name
     * @param b Prefix
     * @param c Suffix
     * @param d Muffix
     * @param e ShownGroup
     * @param f Format
     */
    public void createNewGroup(String a, String b, String c, String d, String e,
                                  String f) {
        ConfigurationSection groups = config.getConfigurationSection("groups");
        if (groups.contains(a)) {
            throw new UnsupportedOperationException(
                    "Cannot create duplicate group in config!");
        }
        groups.createSection(a);
        ConfigurationSection keys = groups.getConfigurationSection(a);
        keys.set("Prefix", b);
        keys.set("Suffix", c);
        keys.set("Muffix", d);
        keys.set("ShownGroup", e);
        keys.set("Format", f);
        this.saveConfig();
    }
    
    public void editGroup(String group, String key, String value) {
        ConfigurationSection groups = config.getConfigurationSection("groups");
        if (groups.contains(group)) {
            ConfigurationSection keys = groups.getConfigurationSection(group);
            keys.set(key, value);            
            this.saveConfig();
        }
    }

    /**
     * Api method to delete a group from config
     *
     * @param group
     * @throws NullPointerException if the group doesn't exist
     */
    public void deleteGroup(final String group) {
        ConfigurationSection groups = config.getConfigurationSection("groups");
        if (groups.contains(group)) {
            groups.set(group, null);
        } else {
            throw new NullPointerException("Group does not exist");
        }
        this.saveConfig();
    }

    protected FileConfiguration getYaml() {
        return this.config;
    }

    protected void addDefaults() {
        config.options().copyDefaults(true);
        saveConfig();
    }

    protected abstract void loadKeys();
}