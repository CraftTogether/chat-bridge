package xyz.crafttogether.chatbridge.configuration.sections;

import xyz.crafttogether.chatbridge.configuration.ConfigSection;

public class DiscordConfigSection extends ConfigSection {
    private long guildId;
    private long channelId;
    private String prefix;
    private String webhook;

    public DiscordConfigSection(long guildId, long discordId, String prefix, String webhook) {
        this.guildId = guildId;
        this.channelId = discordId;
        this.prefix = prefix;
        this.webhook = webhook;
    }

    public long getGuildId() {
        return guildId;
    }

    public long getChannelId() {
        return channelId;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getWebhook() {
        return webhook;
    }
}
