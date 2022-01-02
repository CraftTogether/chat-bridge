package com.github.crafttogether.chatbridge;

public enum MessageSource {
    MINECRAFT(ChatBridge.plugin.getConfig().getConfigurationSection("discord").getString("minecraftPrefix")),
    DISCORD(""),
    IRC(ChatBridge.plugin.getConfig().getConfigurationSection("irc").getString("prefix"));

    public final String icon;
    MessageSource(String icon) {
        this.icon = icon;
    }
}
