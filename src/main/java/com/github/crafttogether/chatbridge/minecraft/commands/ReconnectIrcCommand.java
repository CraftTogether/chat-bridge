package com.github.crafttogether.chatbridge.minecraft.commands;

import com.github.crafttogether.chatbridge.ChatBridge;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ReconnectIrcCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "You have not specified whether to force reconnect or not (true/false)");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "true":
                if (ChatBridge.getIrcThread().getClient().isConnected()) {
                    try {
                        ChatBridge.getIrcThread().getClient().command.disconnect("Restarting chat bridge plugin");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ChatBridge.createIrcConnection();
                break;
            case "false":
                if (!ChatBridge.isIrcConnected()) {
                    ChatBridge.createIrcConnection();
                } else {
                    sender.sendMessage(ChatColor.RED + "IRC client already connected");
                }
                break;
            default:
                sender.sendMessage(String.format("%sMust me true or false, not %s", ChatColor.RED, args[0]));
                break;
        }
        return true;
    }
}
