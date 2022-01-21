package com.github.crafttogether.chatbridge.minecraft.commands;

import com.github.crafttogether.chatbridge.ChatBridge;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IrcCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid usage");
            sender.sendMessage(ChatColor.DARK_PURPLE + "IRC Commands:\nreconnect - reconnect to the IRC server\ndisconnect - disconnect from the IRC server");
            return true;
        }

        switch (args[0]) {
            case "reconnect" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Invalid usage: /irc reconnect <true/false>");
                    break;
                }
                if (args[1].equalsIgnoreCase("false")) {
                    if (!ChatBridge.isIrcConnected()) {
                        ChatBridge.createIrcConnection();
                    } else {
                        sender.sendMessage(ChatColor.RED + "IRC client already connected");
                    }
                } else if (args[1].equals("true")) {
                    if (ChatBridge.getIrcThread().getClient().isConnected()) {
                        try {
                            ChatBridge.getIrcThread().getClient().command.disconnect("Restarting chat bridge");
                            ChatBridge.getIrcThread().join();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ChatBridge.createIrcConnection();
                } else {
                    sender.sendMessage(String.format("%sInvalid usage: true/false not '%s'", ChatColor.RED, args[2]));
                }
            }
            case "disconnect" -> {
                if (!ChatBridge.isIrcEnabled()) {
                    sender.sendMessage(ChatColor.RED + "IRC disabled in config");
                    return true;
                }
                try {
                    ChatBridge.getIrcThread().getClient().command.disconnect("Disconnected by " + sender.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            default -> sender.sendMessage(String.format("%sThere is no IRC called %s", ChatColor.RED, args[1]));
        }
        return true;
    }
}
