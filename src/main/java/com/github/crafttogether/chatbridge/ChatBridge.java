package com.github.crafttogether.chatbridge;

import com.github.crafttogether.chatbridge.discord.DiscordBot;
import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import com.github.crafttogether.chatbridge.irc.OnDisconnect;
import com.github.crafttogether.chatbridge.irc.OnPrivMessage;
import com.github.crafttogether.chatbridge.irc.OnWelcomeMessage;
import com.github.crafttogether.chatbridge.minecraft.*;
import dev.polarian.ircj.IrcClient;
import dev.polarian.ircj.objects.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ChatBridge extends JavaPlugin {

    private static final Logger logger = LoggerFactory.getLogger(ChatBridge.class);

    private static JavaPlugin plugin;
    private static IrcClient ircClient;

    // Variables to store the connection state of discord and irc to ensure they are both connected
    private static boolean discordConnected = false;
    private static boolean ircConnected = false;
    private static boolean pluginLoaded = false;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        try {
            getConfig().load(Files.newBufferedReader(Path.of(plugin.getDataFolder() + "/config.yml")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ConfigurationSection section = this.getConfig().getConfigurationSection("irc");
        assert section != null;
        final Config config = new Config();

        final List<String> ircChannel = new ArrayList<String>() {{
            add("#" + section.getString("channel"));
        }};
        final String nickname = section.getString("username");

        config
                .setUsername(nickname)
                .setNickname(nickname)
                .setChannels(ircChannel)
                .setHostname(section.getString("hostname"))
                .setPort(section.getInt("port"))
                .setTimeout(section.getInt("timeout") * 1000) // multiply by 1000 to convert seconds to milliseconds
                .setTls(section.getBoolean("tls"));

        ircClient = new IrcClient(config);
        ircClient.addWelcomeEventListener(new OnWelcomeMessage());
        ircClient.addPrivMessageEventListener(new OnPrivMessage());
        ircClient.addDisconnectEventListener(new OnDisconnect());

        IrcMessageSender.channel = ircChannel.get(0);
        IrcMessageSender.client = ircClient;

        // Discord thread
        new Thread(() -> {
            logger.info("Discord Thread started");
            try {
                DiscordBot.start();
            } catch (LoginException e) {
                e.printStackTrace();
            }
        }).start();

        // IRC thread
        new Thread(() -> {
            try {
                logger.info("IRC Thread started");
                ircClient.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        pluginLoaded = true;
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatBridge is active");
        registerEvents();
    }

    @Override
    public void onDisable() {
        pluginLoaded = false;
    }

    private void registerEvents() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new MinecraftMessageListener(), this);
        pluginManager.registerEvents(new MinecraftJoinEvent(), this);
        pluginManager.registerEvents(new MinecraftQuitEvent(), this);
        Bukkit.getPluginCommand("verify").setExecutor(new VerifyCommand());
        Bukkit.getPluginCommand("unlink").setExecutor(new UnlinkCommand());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static IrcClient getIrcClient() {
        return ircClient;
    }

    public static boolean isIrcConnected() {
        return ircConnected;
    }

    public static void setIrcConnected(boolean connected) {
        ircConnected = connected;
    }

    public static void setDiscordConnected(boolean connected) {
        discordConnected = connected;
    }

    public static boolean getDiscordConnected() {
        return discordConnected;
    }

    public static boolean isLoaded() {
        return pluginLoaded;
    }
}