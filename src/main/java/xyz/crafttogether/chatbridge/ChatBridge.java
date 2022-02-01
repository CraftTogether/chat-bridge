package xyz.crafttogether.chatbridge;

import dev.polarian.ircj.IrcClient;
import dev.polarian.ircj.UserMode;
import dev.polarian.ircj.objects.Config;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.chatbridge.configuration.ConfigHandler;
import xyz.crafttogether.chatbridge.configuration.sections.IrcConfigSection;
import xyz.crafttogether.chatbridge.discord.Command;
import xyz.crafttogether.chatbridge.discord.DiscordListener;
import xyz.crafttogether.chatbridge.discord.DiscordMessageSender;
import xyz.crafttogether.chatbridge.discord.commands.OnlineCommand;
import xyz.crafttogether.chatbridge.irc.IrcConnection;
import xyz.crafttogether.chatbridge.irc.OnDisconnect;
import xyz.crafttogether.chatbridge.irc.OnPrivMessage;
import xyz.crafttogether.chatbridge.irc.OnWelcomeMessage;
import xyz.crafttogether.chatbridge.minecraft.commands.IrcCommand;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftJoinEvent;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftMessageListener;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftQuitEvent;
import xyz.crafttogether.chatbridge.minecraft.listeners.WegListener;
import xyz.crafttogether.kelp.Kelp;
import xyz.crafttogether.weg.EventListener;
import xyz.crafttogether.weg.Weg;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatBridge extends JavaPlugin {

    private static final Logger logger = LoggerFactory.getLogger(ChatBridge.class);

    private static final HashMap<String, Command> discordCommands = new HashMap<>();

    private static JavaPlugin plugin;
    private static IrcConnection ircConnection;

    private static int remainingAttempts;

    private static EventListener wegListener;

    // Variables to store the connection state of discord and irc to ensure they are both connected
    private static boolean ircConnected = false;

    public static void createIrcConnection() {
        if (ircConnection != null) {
            if (ircConnection.isAlive()) {
                logger.error("Attempted to create IRC connection, however connection already exists");
                return;
            }
        }
        final Config config = new Config();
        IrcConfigSection section = ConfigHandler.getConfig().getIrcConfigSection();

        final List<String> ircChannel = new ArrayList<>() {{add("#" + section.getChannel());}};
        final String nickname = section.getUsername();
        config
                .setUsername(nickname)
                .setNickname(nickname)
                .setChannels(ircChannel)
                .setHostname(section.getHostname())
                .setPort(section.getPort())
                .setTimeout(section.getTimeout() * 1000) // multiply by 1000 to convert seconds to milliseconds
                .setTls(section.isTlsEnabled())
                .setRealName(nickname)
                .setUserMode(UserMode.NONE);

        IrcClient ircClient = new IrcClient(config);
        ircClient.addWelcomeEventListener(new OnWelcomeMessage());
        ircClient.addPrivMessageEventListener(new OnPrivMessage());
        ircClient.addDisconnectEventListener(new OnDisconnect());

        ircConnection = new IrcConnection(ircClient);
        ircConnection.start();
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
        remainingAttempts = ConfigHandler.getConfig().getIrcConfigSection().getReconnectAttempts();
    }

    public static int getRemainingAttempts() {
        return remainingAttempts;
    }

    public static void decrementRemainingAttempts() {
        remainingAttempts--;
    }

    public static void addDiscordCommand(Command command) {
        Kelp.getClient().upsertCommand(command.getName(), command.getDescription()).queue();
        discordCommands.put(command.getName(), command);
    }

    @Nullable
    public static Command getDiscordCommand(String commandName) {
        return discordCommands.getOrDefault(commandName, null);
    }

    public static void updateChannelStatistics(int onlinePlayers, int afkPlayers) {
        try {
            ircConnection.getClient().getCommands().setTopic(ConfigHandler.getConfig().getIrcConfigSection().getChannel(), String.format("There are %d players online, %d of which are AFK", onlinePlayers, afkPlayers));
        } catch (IOException e) {
            logger.error("Failed to update IRC topic");
            e.printStackTrace();
        }
        TextChannel channel = Kelp.getClient().getTextChannelById(plugin.getConfig().getLong("discord.discordChannelId"));
    }

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        ConfigHandler.loadConfig();
        if (ConfigHandler.getConfig().getIrcConfigSection().isEnabled()) {
            createIrcConnection();
        }
        Kelp.addListeners(new DiscordListener());
        addDiscordCommand(new OnlineCommand());
        DiscordMessageSender.send("Server", ":white_check_mark: Chat bridge enabled", null, MessageSource.OTHER);

        wegListener = new WegListener();
        Weg.addListener(wegListener);

        registerEvents();
        updateChannelStatistics(Bukkit.getOnlinePlayers().size(), Weg.getAfkPlayers());
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatBridge is active");
    }

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

    private void registerEvents() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new MinecraftMessageListener(), this);
        pluginManager.registerEvents(new MinecraftJoinEvent(), this);
        pluginManager.registerEvents(new MinecraftQuitEvent(), this);
        getCommand("irc").setExecutor(new IrcCommand());
    }
}