package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.discord.LinkCommand;
import com.github.crafttogether.chatbridge.utilities.Members;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class VerifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final Player player = (Player) sender;

        // Command usage
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /verify <code>"); // Send error message
            return true;
        }

        // Provided code is valid
        if (LinkCommand.verify.containsKey(args[0])) {
            final String discordId = LinkCommand.verify.get(args[0]);
            final JSONArray members = Members.get(); // Get list of connected accounts

            // Search for already existing connection
            for (int i = 0; i < members.length(); i++) {
                final JSONObject member = members.getJSONObject(i); // Get current member
                // Member already exists
                if (member.getString("discord").equals(discordId)) {
                    LinkCommand.verify.remove(args[0]);
                    player.sendMessage(ChatColor.RED + "You're discord account is already linked up. To unlink it use /unlink"); // Send error message
                    return true;
                }
            }

            // Create JSONObject for member
            final JSONObject member = new JSONObject()
                    .put("discord", discordId) // Add discord id
                    .put("minecraft", player.getUniqueId()); // Add minecraft UUID
            members.put(member); // Add member to file
            Members.update(members); // Update members file
            player.sendMessage(ChatColor.GREEN + "Verification successfully"); // Send success message
        }
        // Provided code is invalid
        else {
            player.sendMessage(ChatColor.RED + "Invalid verification code or your verification code timed out!"); // Send error message
        }

        return true; // Return true for no errors
    }
}
