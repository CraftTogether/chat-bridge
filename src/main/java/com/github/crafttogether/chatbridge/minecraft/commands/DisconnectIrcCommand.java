package com.github.crafttogether.chatbridge.minecraft.commands;

import com.github.crafttogether.chatbridge.ChatBridge;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DisconnectIrcCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!ChatBridge.isIrcEnabled()) {
            sender.sendMessage(ChatColor.RED + "IRC disabled in config");
            return true;
        }
        try {
            ChatBridge.getIrcThread().getClient().command.disconnect("Disconnected by " + sender.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
