package xyz.crafttogether.chatbridge.minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class containing the IRC commands
 */
public class IrcCommand implements CommandExecutor, TabCompleter {
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
                        if (!ChatBridge.getIrcThread().getClient().isConnected()) {
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
                if (!ConfigHandler.getConfig().isIrcEnabled()) {
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completion = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                completion.add("reconnect");
                completion.add("disconnect");
            }

            case 2 -> {
                if (args[0].equals("reconnect")) {
                    completion.add("true");
                    completion.add("false");
                }
            }
        }
        return completion;
    }
}
