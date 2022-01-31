package xyz.crafttogether.chatbridge;

import com.github.crafttogether.kelp.Kelp;
import dev.polarian.ircj.IrcClient;
import dev.polarian.ircj.UserMode;
import dev.polarian.ircj.objects.Config;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.chatbridge.discord.Command;
import xyz.crafttogether.chatbridge.discord.DiscordMessageSender;
import xyz.crafttogether.chatbridge.discord.DiscordListener;
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
import xyz.crafttogether.weg.EventListener;
import xyz.crafttogether.weg.Weg;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatBridge extends JavaPlugin {

    private static final Logger logger = LoggerFactory.getLogger(ChatBridge.class);

    private static final HashMap<String, Command> discordCommands = new HashMap<>();

    private static JavaPlugin plugin;
    private static IrcConnection ircConnection;

    private static int reconnectAttempts;
    private static int reconnectDelay;
    private static int remainingAttempts;
    private static boolean ircEnabled;
    private static String channel;

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
        channel = ircChannel.get(0);
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

    public static boolean isIrcEnabled() {
        return ircEnabled;
    }

    public static String getChannel() {
        return channel;
    }

    public static void addDiscordCommand(Command command) {
        Kelp.getClient().upsertCommand(command.getName(), command.getDescription()).queue();
        discordCommands.put(command.getName(), command);
    }

    @Nullable
    public static Command getDiscordCommand(String commandName) {
        return discordCommands.getOrDefault(commandName, null);
    }

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
        ircEnabled = getConfig().getConfigurationSection("irc").getBoolean("enabled");
        if (ircEnabled) {
            createIrcConnection();
        }
        Kelp.addListeners(new DiscordListener());
        addDiscordCommand(new OnlineCommand());
        DiscordMessageSender.send("Server", ":white_check_mark: Chat bridge enabled", null, MessageSource.OTHER);

        wegListener = new WegListener();
        Weg.addListener(wegListener);

        registerEvents();
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