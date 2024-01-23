package com.panosnikolakakis.coordmanager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CoordManager extends JavaPlugin {

    private final Map<String, String> savedLocations = new HashMap<>();
    private final SaveLocationCommand saveLocationCommand = new SaveLocationCommand(savedLocations, new File(getDataFolder(), "locations.yml"));
    private final FindLocationCommand findLocationCommand = new FindLocationCommand(savedLocations, new File(getDataFolder(), "locations.yml"));
    private final ListLocationsCommand listLocationsCommand = new ListLocationsCommand(saveLocationCommand.getSavedLocations());
    private final DeleteLocationCommand deleteLocationCommand = new DeleteLocationCommand(savedLocations, new File(getDataFolder(), "locations.yml"));


    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Enabled.");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // Load saved locations from the config file
        loadLocationsFromConfig();

        // Register the "locationsave" command
        getCommand("locationsave").setExecutor(saveLocationCommand);

        // Register the "locationfind" command
        getCommand("locationfind").setExecutor(findLocationCommand);
        getCommand("locationfind").setTabCompleter(findLocationCommand);

        // Register the "locationlist" command
        getCommand("locationlist").setExecutor(listLocationsCommand);

        // Register the "locationdelete" command
        getCommand("locationdelete").setExecutor(deleteLocationCommand);
        getCommand("locationdelete").setTabCompleter(deleteLocationCommand);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabled.");
    }

    private void loadLocationsFromConfig() {
        File configFile = new File(getDataFolder(), "locations.yml");
        if (configFile.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            if (config.contains("locations")) {
                for (String key : config.getConfigurationSection("locations").getKeys(false)) {
                    savedLocations.put(key, config.getString("locations." + key));
                }
            }
        }
    }
}