package xyz.crafttogether.chatbridge;

import dev.polarian.ircj.IrcClient;
import dev.polarian.ircj.objects.Config;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.chatbridge.discord.DiscordListener;
import xyz.crafttogether.chatbridge.discord.DiscordMessageSender;
import xyz.crafttogether.chatbridge.discord.commands.DiscordOnlineCommand;
import xyz.crafttogether.chatbridge.irc.CommandHandler;
import xyz.crafttogether.chatbridge.irc.IrcConnection;
import xyz.crafttogether.chatbridge.irc.IrcEventSubscriber;
import xyz.crafttogether.chatbridge.irc.commands.InvalidCommand;
import xyz.crafttogether.chatbridge.irc.commands.IrcOnlineCommand;
import xyz.crafttogether.chatbridge.minecraft.commands.IrcCommand;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftJoinEvent;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftMessageListener;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftQuitEvent;
import xyz.crafttogether.chatbridge.minecraft.listeners.WegListener;
import xyz.crafttogether.craftcore.CraftCore;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;
import xyz.crafttogether.weg.EventListener;
import xyz.crafttogether.weg.Weg;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;

/**
 * Main class for the plugin, extends the spigot JavaPlugin class
 */
public class ChatBridge extends JavaPlugin {

    /**
     * SLF4J Logger instance
     */
    private static final Logger logger = LoggerFactory.getLogger(ChatBridge.class);

    /**
     * A static instance of the JavaPlugin class (self)
     */
    private static JavaPlugin plugin;
    /**
     * Irc Thread, handles the IRC connection
     */
    private static IrcConnection ircConnection;

    /**
     * A variable storing the number of remaining attempts to connect to the IRC server before it is abandoned and
     * has to be manually connected through the irc connection command
     */
    private static int remainingAttempts;

    /**
     * The instance of the Weg event listener so that the event can be unsubscribed when the plugin is unloaded
     */
    private static EventListener wegListener;

    /**
     * A static method used to creating the IRC connection. Will only connect if there is no existing connection or
     * if the thread has exited.
     */
    public static void createIrcConnection() {
        // Check if already connected to the IRC server
        if (ircConnection != null) {
            if (ircConnection.isAlive()) {
                logger.error("Attempted to create IRC connection, however connection already exists");
                return;
            }
        }

        // Get the IRC configuration from the ConfigHandler and create IRC Config
        final Config config = new Config(ConfigHandler.getConfig().getIrcHostname(), ConfigHandler.getConfig().getIrcPort(),
                ConfigHandler.getConfig().getIrcUsername(), "#" + ConfigHandler.getConfig().getIrcChannel(),
                ConfigHandler.getConfig().isIrcUseTls());

        // Create IRC client and add listeners
        IrcClient ircClient = new IrcClient(config);
        ircClient.addListener(new IrcEventSubscriber());

        // create the IRC connection and start it
        ircConnection = new IrcConnection(ircClient);
        ircConnection.start();
    }

    /**
     * Gets the static instance of the plugin
     * @return The JavaPlugin object
     */
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the IRC connection (extends thread)
     * @return IrcConnection which extends the Thread class
     */
    public static IrcConnection getIrcThread() {
        return ircConnection;
    }

    /**
     * Reset reconnect attempts variable
     */
    public static void resetAttempts() {
        remainingAttempts = ConfigHandler.getConfig().getIrcReconnectAttempts();
    }

    /**
     * Gets the number of remaining attempts to connect to the IRC server
     * @return The number of remaining attempts to connect to the IRC server
     */
    public static int getRemainingAttempts() {
        return remainingAttempts;
    }

    /**
     * Decrements the remaining attempts by 1 when method is invoked
     */
    public static void decrementRemainingAttempts() {
        remainingAttempts--;
    }

    /**
     * Updates the discord and IRC channel topic --> which contains info about the status of the plugin
     *
     * @param onlinePlayers The number of players currently online on the server
     * @param afkPlayers The number of players currently AFK on the server
     */
    public static void updateChannelStatistics(int onlinePlayers, int afkPlayers) {
        String topic = String.format("There are %d players online, %d of which are AFK", onlinePlayers, afkPlayers);
        if (ConfigHandler.getConfig().isIrcEnabled()) {
            try {
                ircConnection.getClient().getCommands().setTopic(ConfigHandler.getConfig().getIrcChannel(), topic);
            } catch (IOException e) {
                logger.error("Failed to update IRC topic");
                e.printStackTrace();
            }
        }
        TextChannel channel = CraftCore.getJda().getTextChannelById(ConfigHandler.getConfig().getDiscordChannelId());
        if (channel == null) {
            logger.error("Failed to get discord channel");
            return;
        }
        channel.getManager().setTopic(topic).queue();
    }

    /**
     * Invoked when the plugin is enabled
     */
    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        ConfigHandler.loadConfig();
        if (ConfigHandler.getConfig().isIrcEnabled()) {
            createIrcConnection();
        }
        CraftCore.addListeners(new DiscordListener());
        CraftCore.addDiscordCommand(new DiscordOnlineCommand());
        DiscordMessageSender.send("Server", ":white_check_mark: Chat bridge enabled", null, MessageSource.OTHER);

        wegListener = new WegListener();
        Weg.addListener(wegListener);

        registerEvents();
        try {
            if (ConfigHandler.getConfig().isIrcEnabled()) {
                ircConnection.getClient().awaitReady();
                CommandHandler.setInvalidCommandHandler(new InvalidCommand());
                CommandHandler.addCommand("online", new IrcOnlineCommand());
            }
            CraftCore.getJda().awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateChannelStatistics(Bukkit.getOnlinePlayers().size(), Weg.getAfkPlayers());
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatBridge is active");
    }

    /**
     * Invoked when the plugin is disabled
     */
    @Override
    public void onDisable() {
        Weg.removeListener(wegListener);
        DiscordMessageSender.send("Server", ":octagonal_sign: Chat bridge disabled",  null, MessageSource.OTHER);
        try {
            if (ircConnection != null) {
                if (ircConnection.isAlive()) {
                    ircConnection.getClient().getCommands().disconnect("Chat Bridge has been disabled");
                    ircConnection.join();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers the required minecraft events
     */
    private void registerEvents() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new MinecraftMessageListener(), this);
        pluginManager.registerEvents(new MinecraftJoinEvent(), this);
        pluginManager.registerEvents(new MinecraftQuitEvent(), this);
        IrcCommand ircCommand = new IrcCommand();
        getCommand("irc").setExecutor(ircCommand);
        getCommand("irc").setTabCompleter(ircCommand);
    }
}