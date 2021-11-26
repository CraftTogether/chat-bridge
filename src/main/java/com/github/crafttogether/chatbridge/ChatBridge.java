package com.github.crafttogether.chatbridge;

import com.github.crafttogether.chatbridge.discord.DiscordBot;
import com.github.crafttogether.chatbridge.minecraft.MinecraftMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public class ChatBridge extends JavaPlugin {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        try {
            plugin = this;
            DiscordBot.start();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatBridge is active");

            final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
            pluginManager.registerEvents(new MinecraftMessageListener(), this);
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