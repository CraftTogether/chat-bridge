package xyz.crafttogether.chatbridge;


import xyz.crafttogether.craftcore.configuration.ConfigHandler;

/**
 * Enum containing all the sources (platforms) where messages can be received from.
 */
public enum MessageSource {
    MINECRAFT(ConfigHandler.getConfig().getMinecraftPrefix()),
    DISCORD(""),
    DISCORD_REFERENCE(""),
    IRC(ConfigHandler.getConfig().getIrcPrefix()),
    OTHER("");

    public final String icon;

    MessageSource(String icon) {
        this.icon = icon;
    }
}
