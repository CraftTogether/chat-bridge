package xyz.crafttogether.chatbridge.minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.configuration.ConfigHandler;

import java.io.IOException;

public class IrcCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "IRC Commands:\nreconnect - reconnect to the IRC server\ndisconnect - disconnect from the IRC server");
            return true;
        }

        switch (args[0]) {
            case "reconnect" -> {
                if (!sender.hasPermission("chatbridge.irc.reconnect")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Invalid usage: /irc reconnect <true/false>");
                    break;
                }
                switch (args[1]) {
                    case "false" -> {
                        if (!ChatBridge.isIrcConnected()) {
                            ChatBridge.createIrcConnection();
                        } else {
                            sender.sendMessage(ChatColor.RED + "IRC client already connected");
                        }
                    }

                    case "true" -> {
                        if (ChatBridge.getIrcThread().getClient().isConnected()) {
                            try {
                                ChatBridge.getIrcThread().getClient().getCommands().disconnect("Restarting chat bridge");
                                ChatBridge.getIrcThread().join();
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            ChatBridge.createIrcConnection();
                        }
                    }

                    default -> sender.sendMessage(String.format("%sInvalid usage: true/false not '%s'", ChatColor.RED, args[2]));
                }
            }
            case "disconnect" -> {
                if (!sender.hasPermission("chatbridge.irc.disconnect")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command");
                    return true;
                }
                if (!ConfigHandler.getConfig().getIrcConfigSection().isEnabled()) {
                    sender.sendMessage(ChatColor.RED + "IRC disabled in config");
                    return true;
                }
                try {
                    ChatBridge.getIrcThread().getClient().getCommands().disconnect("Disconnected by " + sender.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            default -> sender.sendMessage(String.format("%sThere is no IRC command called %s", ChatColor.RED, args[1]));
        }
        return true;
    }
}
