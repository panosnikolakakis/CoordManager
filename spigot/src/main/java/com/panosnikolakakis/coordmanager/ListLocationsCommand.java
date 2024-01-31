package com.panosnikolakakis.coordmanager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class ListLocationsCommand implements CommandExecutor {

    private final Map<String, String> savedLocations;

    public ListLocationsCommand(Map<String, String> savedLocations) {
        this.savedLocations = savedLocations;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 0 || !label.equalsIgnoreCase("locationlist")) {
            player.sendMessage("Usage: /locationlist");
            return true;
        }

        if (savedLocations.isEmpty()) {
            player.sendMessage("No locations saved yet.");
        } else {
            player.sendMessage("Saved Locations:");
            for (Map.Entry<String, String> entry : savedLocations.entrySet()) {
                String locationName = entry.getKey();
                String locationDetails = entry.getValue();

                // Color the dimension names
                locationDetails = locationDetails.replace("Overworld", ChatColor.GREEN + "Overworld" + ChatColor.RESET);
                locationDetails = locationDetails.replace("Nether", ChatColor.RED + "Nether" + ChatColor.RESET);
                locationDetails = locationDetails.replace("The End", ChatColor.DARK_PURPLE + "The End" + ChatColor.RESET);

                player.sendMessage("- " + ChatColor.GOLD + locationName + ChatColor.RESET + ": " + locationDetails);
            }
        }

        return true;
    }
}