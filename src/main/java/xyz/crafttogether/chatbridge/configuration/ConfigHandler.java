package xyz.crafttogether.chatbridge.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.crafttogether.chatbridge.ChatBridge;

import java.io.File;

/**
 * Handles the static configuration, and provides methods to load, reload and get the plugin configuration
 */
public class ConfigHandler {
    /**
     * The Config instance
     */
    private static Config config;
    /**
     * The config file, used for reloading the configuration
     */
    private static File configFile;

    /**
     * Initialises the config, should only be called once
     */
    public static void loadConfig() {
        configFile = new File(ChatBridge.getPlugin().getDataFolder() + "/config.yml");
        reloadConfig();
    }

    /**
     * Reloads the configuration from the config file
     */
    public static void reloadConfig() {
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
        config = new Config(
                fileConfiguration.getLong("discord.guildId"),
                fileConfiguration.getLong("discord.discordChannelId"),
                fileConfiguration.getString("discord.minecraftPrefix"),
                fileConfiguration.getString("discord.webhook"),
                fileConfiguration.getBoolean("irc.enabled"),
                fileConfiguration.getString("irc.username"),
                fileConfiguration.getString("irc.hostname"),
                fileConfiguration.getInt("irc.port"),
                fileConfiguration.getBoolean("irc.tls"),
                fileConfiguration.getInt("irc.timeout"),
                fileConfiguration.getString("irc.channel"),
                fileConfiguration.getString("irc.prefix"),
                fileConfiguration.getInt("irc.reconnectAttempts"),
                fileConfiguration.getInt("irc.reconnectDelay"),
                fileConfiguration.getString("irc.commandPrefix")
        );
    }

    /**
     * Gets the Config object
     *
     * @return The Config object
     */
    public static Config getConfig() {
        return config;
    }
}
