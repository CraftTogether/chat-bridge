package com.github.crafttogether.chatbridge;

public enum MessageSource {
    MINECRAFT(ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("minecraftPrefix")),
    DISCORD(""),
    IRC(ChatBridge.getPlugin().getConfig().getConfigurationSection("irc").getString("prefix"));

    public final String icon;

    MessageSource(String icon) {
        this.icon = icon;
    }
}
