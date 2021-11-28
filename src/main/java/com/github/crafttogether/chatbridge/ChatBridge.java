package com.github.crafttogether.chatbridge;

import com.github.crafttogether.chatbridge.discord.DiscordBot;
import com.github.crafttogether.chatbridge.minecraft.MinecraftMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatBridge extends JavaPlugin {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        new Thread(DiscordBot::start).start();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatBridge is active");

        registerEvents();
    }

    private void registerEvents() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new MinecraftMessageListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}