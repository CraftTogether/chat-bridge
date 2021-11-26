package com.github.crafttogether.chatbridge;

import com.github.crafttogether.chatbridge.discord.DiscordBot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class ChatBridge extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            plugin = this;
            DiscordBot.start();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatBridge is active");
        } catch (LoginException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error while starting the Plugin \"ChatBridge\"");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
