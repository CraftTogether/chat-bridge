package xyz.crafttogether.chatbridge;

import xyz.crafttogether.chatbridge.configuration.ConfigHandler;

/**
 * Enum containing all the sources (platforms) where messages can be received from.
 */
public enum MessageSource {
    MINECRAFT(ConfigHandler.getConfig().getDiscordConfigSection().getPrefix()),
    DISCORD(""),
    IRC(ConfigHandler.getConfig().getIrcConfigSection().getPrefix()),
    OTHER("");

    public final String icon;

    MessageSource(String icon) {
        this.icon = icon;
    }
}
