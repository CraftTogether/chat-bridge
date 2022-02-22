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

import java.io.IOException;

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
     * A variable storing the number of remaining attempts to connect to the IRC server before it is abandoned and
     * has to be manually connected through the irc connection command
     */
    private static int remainingAttempts;
    /**
     * The instance of the Weg event listener so that the event can be unsubscribed when the plugin is unloaded
     */
    private static EventListener wegListener;
    /**
     * The IRC client
     */
    private static IrcClient ircClient;

    /**
     * Gets the static instance of the plugin
     * @return The JavaPlugin object
     */
    public static JavaPlugin getPlugin() {
        return plugin;
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
     * Gets the IRC client
     *
     * @return The IRC client
     */
    public static IrcClient getIrcClient() {
        return ircClient;
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
                ircClient.getCommands().setTopic(ConfigHandler.getConfig().getIrcChannel(), topic);
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
     * Connect to the IRC server if not already connected
     */
    public static void connectIrc() {
        if (ircClient != null) if (ircClient.isConnected()) return;
        if (!ConfigHandler.getConfig().isIrcEnabled()) return;
        Config config = new Config(
                ConfigHandler.getConfig().getIrcHostname(),
                ConfigHandler.getConfig().getIrcPort(),
                ConfigHandler.getConfig().getIrcUsername(),
                ConfigHandler.getConfig().getIrcChannel(),
                ConfigHandler.getConfig().isIrcEnabled()
        );
        ircClient = new IrcClient(config);
        ircClient.addListener(new IrcEventSubscriber());
        ircClient.connect();
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
        connectIrc();
        CraftCore.addListeners(new DiscordListener());
        CraftCore.addDiscordCommand(new DiscordOnlineCommand());
        DiscordMessageSender.send("Server", ":white_check_mark: Chat bridge enabled", null, MessageSource.OTHER);

        wegListener = new WegListener();
        Weg.addListener(wegListener);

        registerEvents();
        try {
            if (ConfigHandler.getConfig().isIrcEnabled()) {
                ircClient.awaitReady();
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
        if (ConfigHandler.getConfig().isIrcEnabled()) {
            if (ircClient.isConnected()) {
                try {
                    ircClient.getCommands().disconnect("Chat bridge has been disabled");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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