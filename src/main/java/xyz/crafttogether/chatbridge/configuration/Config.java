package xyz.crafttogether.chatbridge.configuration;

import xyz.crafttogether.chatbridge.configuration.sections.ConfigSections;
import xyz.crafttogether.chatbridge.configuration.sections.DiscordConfigSection;
import xyz.crafttogether.chatbridge.configuration.sections.IrcConfigSection;

import java.util.HashMap;

public class Config {
    private final DiscordConfigSection discordConfigSection;
    private final IrcConfigSection ircConfigSection;
    private final HashMap<ConfigSections, ConfigSection> configSections = new HashMap<>();

    public Config(long guildId, long discordId, String discordPrefix, String webhook, boolean ircEnabled,
                  String ircUsername, String ircHostname, int port, boolean tls, int timeout, String ircChannel,
                  String ircPrefix, int reconnectAttempts, int reconnectDelay) {
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
                reconnectDelay
        );
        configSections.put(ConfigSections.IRC, discordConfigSection);
    }

    public DiscordConfigSection getDiscordConfigSection() {
        return discordConfigSection;
    }

    public IrcConfigSection getIrcConfigSection() {
        return ircConfigSection;
    }

    public HashMap<ConfigSections, ConfigSection> getConfigSections() {
        return configSections;
    }
}
