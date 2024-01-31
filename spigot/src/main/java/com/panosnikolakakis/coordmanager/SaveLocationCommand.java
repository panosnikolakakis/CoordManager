package com.panosnikolakakis.coordmanager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SaveLocationCommand implements CommandExecutor {
    private final Map<String, String> savedLocations;
    private final File configFile;

    public SaveLocationCommand(Map<String, String> savedLocations, File configFile) {
        this.savedLocations = savedLocations;
        this.configFile = configFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Usage: /locationsave <LocationName>");
            return true;
        }

        String locationName = String.join(" ", args);  // Concatenate all arguments to form the location name
        Location playerLocation = player.getLocation();

        World world = playerLocation.getWorld();
        double x = playerLocation.getX();
        double y = playerLocation.getY();
        double z = playerLocation.getZ();

        String dimension = getDimensionName(world.getEnvironment());

        savedLocations.put(locationName, dimension + ": " + String.format("%.2f, %.2f, %.2f", x, y, z));

        saveLocationsToConfig();

        player.sendMessage("Location '" + ChatColor.GOLD + locationName + ChatColor.RESET + "' saved at " + dimension + " coordinates: " + x + ", " + y + ", " + z);
        return true;
    }

    private String getDimensionName(World.Environment environment) {
        switch (environment) {
            case NETHER:
                return ChatColor.RED + "Nether" + ChatColor.RESET;
            case THE_END:
                return ChatColor.DARK_PURPLE + "The End" + ChatColor.RESET;
            default:
                return ChatColor.GREEN + "Overworld" + ChatColor.RESET;
        }
    }

    private void saveLocationsToConfig() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        for (Map.Entry<String, String> entry : savedLocations.entrySet()) {
            config.set("locations." + entry.getKey(), entry.getValue());
        }

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getSavedLocations() {
        return savedLocations;
    }
}