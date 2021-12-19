package com.github.crafttogether.chatbridge;

import com.github.crafttogether.chatbridge.discord.DiscordBot;
import com.github.crafttogether.chatbridge.irc.OnPrivMessage;
import com.github.crafttogether.chatbridge.irc.OnWelcomeMessage;
import com.github.crafttogether.chatbridge.minecraft.MinecraftMessageListener;
import dev.polarian.ircj.IrcClient;
import dev.polarian.ircj.objects.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatBridge extends JavaPlugin {

    public static JavaPlugin plugin;
    public static IrcClient client;

    // Variables to store the connection state of discord and irc to ensure they are both connected
    public static boolean discordConnected = false;
    public static boolean ircConnected = false;

    @Override
    public void onEnable() {
        plugin = this;
        ConfigurationSection section = this.getConfig().getConfigurationSection("irc");
        assert section != null;
        Config config = new Config();
        List<String> channel = new ArrayList<>();
        channel.add("#" + section.getString("channel"));
        String nickname = section.getString("username");
        config.setUsername(nickname);
        config.setNickname(nickname);
        config.setChannels(channel);
        config.setHostname(section.getString("hostname"));
        config.setPort(section.getInt("port"));
        config.setTimeout(section.getInt("timeout") * 1000); // multiply by 1000 to convert seconds to milliseconds
        config.setTls(section.getBoolean("tls"));
        client = new IrcClient(config);
        client.addWelcomeEventListener(new OnWelcomeMessage());
        client.addPrivMessageEventListener(new OnPrivMessage());
        new Thread(DiscordBot::start).start();
        new Thread(() -> {
            try {
                client.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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