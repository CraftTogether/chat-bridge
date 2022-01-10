package com.github.crafttogether.chatbridge.minecraft.commands;

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

public class UnlinkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final Player player = (Player) sender;
        final String uuid = player.getUniqueId().toString();

        final JSONArray members = Members.get(); // Get list of connected accounts
        // Search for member
        for (int i = 0; i < members.length(); i++) {
            final JSONObject member = members.getJSONObject(i); // Get current member
            // Member already exists
            if (member.getString("minecraft").equals(uuid)) {
                members.remove(i);
                Members.update(members);
                player.sendMessage(ChatColor.GREEN + "Successfully unlinked your discord connection! To link your discord account back again to your minecraft account, use the command /link in Discord."); // Send error message
                return true;
            }
        }

        // Member hasn't liked up his again yet
        player.sendMessage(ChatColor.RED + "You haven't linked up your account yet. To link your discord account to your minecraft account, use the command /link in Discord."); // Send success message
        return true; // Return true for no errors
    }
}
