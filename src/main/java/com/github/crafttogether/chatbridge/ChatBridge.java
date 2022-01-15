package com.github.crafttogether.chatbridge;

import com.github.crafttogether.chatbridge.discord.LinkCommand;
import com.github.crafttogether.chatbridge.discord.MessageListener;
import com.github.crafttogether.chatbridge.irc.*;
import com.github.crafttogether.chatbridge.minecraft.MinecraftJoinEvent;
import com.github.crafttogether.chatbridge.minecraft.MinecraftMessageListener;
import com.github.crafttogether.chatbridge.minecraft.MinecraftQuitEvent;
import com.github.crafttogether.chatbridge.minecraft.commands.UnlinkCommand;
import com.github.crafttogether.chatbridge.minecraft.commands.VerifyCommand;
import com.github.crafttogether.kelp.Kelp;
import dev.polarian.ircj.IrcClient;
import dev.polarian.ircj.UserMode;
import dev.polarian.ircj.objects.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ChatBridge extends JavaPlugin {

    private static final Logger logger = LoggerFactory.getLogger(ChatBridge.class);

    private static JavaPlugin plugin;
    private static IrcConnection ircConnection;

    private static int reconnectAttempts;
    private static int reconnectDelay;
    private static int remainingAttempts;

    // Variables to store the connection state of discord and irc to ensure they are both connected
    private static boolean ircConnected = false;

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
        createIrcConnection();
        Kelp.addListeners(new MessageListener(), new LinkCommand());

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatBridge is active");
        registerEvents();
    }

    public static void createIrcConnection() {
        if (ircConnection != null) {
            logger.error("Attempted to create a new IRC connection, but IRC connection already exists");
            return;
        }
        final ConfigurationSection section = ChatBridge.getPlugin().getConfig().getConfigurationSection("irc");
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
                .setTls(section.getBoolean("tls"))
                .setRealName(nickname)
                .setUserMode(UserMode.NONE);

        IrcClient ircClient = new IrcClient(config);
        ircClient.addWelcomeEventListener(new OnWelcomeMessage());
        ircClient.addPrivMessageEventListener(new OnPrivMessage());
        ircClient.addDisconnectEventListener(new OnDisconnect());

        reconnectAttempts = section.getInt("reconnectAttempts");
        reconnectDelay = section.getInt("reconnectDelay");
        IrcMessageSender.setChannel(ircChannel.get(0));
        ircConnection = new IrcConnection(ircClient);
        ircConnection.start();
    }

    @Override
    public void onDisable() {
        try {
            ircConnection.getClient().command.disconnect("Chat Bridge has been disabled");
            ircConnection.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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

    public static IrcConnection getIrcThread() {
        return ircConnection;
    }

    public static boolean isIrcConnected() {
        return ircConnected;
    }

    public static void setIrcConnected(boolean connected) {
        ircConnected = connected;
    }

    public static void resetAttempts() {
        remainingAttempts = reconnectAttempts;
    }

    public static int getRemainingAttempts() {
        return remainingAttempts;
    }

    public static void decrementRemainingAttempts() {
        remainingAttempts--;
    }

    public static int getReconnectDelay() {
        return reconnectDelay;
    }
}