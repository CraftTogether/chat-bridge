package xyz.crafttogether.chatbridge.configuration;

import xyz.crafttogether.chatbridge.configuration.sections.ConfigSections;
import xyz.crafttogether.chatbridge.configuration.sections.DiscordConfigSection;
import xyz.crafttogether.chatbridge.configuration.sections.IrcConfigSection;

import java.util.HashMap;

/**
 * Class containing all the ConfigSections, and the constructor to create the Config
 */
public class Config {
    private final DiscordConfigSection discordConfigSection;
    private final IrcConfigSection ircConfigSection;
    private final HashMap<ConfigSections, ConfigSection> configSections = new HashMap<>();

    /**
     * Creates a Config object
     *
     * @param guildId The discord guild ID
     * @param discordId The discord channel ID
     * @param discordPrefix The discord prefix
     * @param webhook The discord webhook
     * @param ircEnabled Whether IRC is enabled
     * @param ircUsername The IRC username
     * @param ircHostname The IRC server hostname or ip
     * @param port The IRC server port
     * @param tls Whether to use tls when connecting to the IRC server
     * @param timeout The connection timeout
     * @param ircChannel The IRC channel which is bridged
     * @param ircPrefix The IRC prefix
     * @param reconnectAttempts The number of attempts to reconnect to the IRC server before it is abandoned
     * @param reconnectDelay The delay between reconnect attempts
     * @param commandPrefix The IRC command prefix
     */
    public Config(long guildId, long discordId, String discordPrefix, String webhook, boolean ircEnabled,
                  String ircUsername, String ircHostname, int port, boolean tls, int timeout, String ircChannel,
                  String ircPrefix, int reconnectAttempts, int reconnectDelay, String commandPrefix) {
        discordConfigSection = new DiscordConfigSection(
                guildId,
                discordId,
                discordPrefix,
                webhook
        );
        configSections.put(ConfigSections.DISCORD, discordConfigSection);

        ircConfigSection = new IrcConfigSection(
                ircEnabled,
                ircUsername,
                ircHostname,
                port,
                tls,
                timeout,
                ircChannel,
                ircPrefix,
                reconnectAttempts,
                reconnectDelay,
                commandPrefix
        );
        configSections.put(ConfigSections.IRC, discordConfigSection);
    }

    /**
     * Gets the discord configuration section
     *
     * @return The discord configuration section
     */
    public DiscordConfigSection getDiscordConfigSection() {
        return discordConfigSection;
    }

    /**
     * Gets the IRC configuration section
     *
     * @return The IRC configuration section
     */
    public IrcConfigSection getIrcConfigSection() {
        return ircConfigSection;
    }

    /**
     * Gets all the configuration sections
     *
     * @return All the configuration sections
     */
    public HashMap<ConfigSections, ConfigSection> getConfigSections() {
        return configSections;
    }
}
