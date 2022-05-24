package com.dreamlightbuilds.commons.bukkit.configs;

import com.dreamlightbuilds.commons.Common;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private final File file;
    private FileConfiguration config;

    public CustomConfig(final String configName, final String directory) {
        final Plugin plugin = Common.getInstance();

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (directory == null) {
            this.file = new File(plugin.getDataFolder(), configName);
        } else {
            final File subFolder = new File(plugin.getDataFolder() + File.separator + directory);
            if (!subFolder.exists()) {
                subFolder.mkdirs();
            }
            this.file = new File(subFolder, configName);
        }

        if (!this.file.exists()) {
            plugin.saveResource(configName, false);
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void saveConfig() {
        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

}
