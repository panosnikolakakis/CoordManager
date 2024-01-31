package com.panosnikolakakis.coordmanager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindLocationCommand implements CommandExecutor, TabCompleter {

    private final Map<String, String> savedLocations;
    private final File configFile;

    public FindLocationCommand(Map<String, String> savedLocations, File configFile) {
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
            player.sendMessage("Usage: /locationfind <LocationName>");
            return true;
        }

        String locationName = String.join(" ", args);

        if (savedLocations.containsKey(locationName)) {
            String locationDetails = savedLocations.get(locationName);

            // Color the dimension names
            locationDetails = locationDetails.replace("Overworld", ChatColor.GREEN + "Overworld" + ChatColor.RESET);
            locationDetails = locationDetails.replace("Nether", ChatColor.RED + "Nether" + ChatColor.RESET);
            locationDetails = locationDetails.replace("The End", ChatColor.DARK_PURPLE + "The End" + ChatColor.RESET);

            player.sendMessage("Location '" + ChatColor.GOLD + locationName + ChatColor.RESET + "' details: " + locationDetails);
        } else {
            player.sendMessage("Location '" + ChatColor.GOLD + locationName + ChatColor.RESET + "' not found.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return getLocationNamesStartingWith(args[0]);
        }
        return null;
    }

    private List<String> getLocationNamesStartingWith(String prefix) {
        List<String> completions = new ArrayList<>();
        for (String locationName : savedLocations.keySet()) {
            if (locationName.toLowerCase().startsWith(prefix.toLowerCase())) {
                completions.add(locationName);
            }
        }
        return completions;
    }
}