package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;

public enum MessageSource {
    MINECRAFT(ChatBridge.plugin.getConfig().getConfigurationSection("discord").getString("minecraftPrefix")),
    DISCORD(""),
    IRC(ChatBridge.plugin.getConfig().getConfigurationSection("irc").getString("prefix"));

    public final String icon;
    MessageSource(String icon) {
        this.icon = icon;
    }
}
