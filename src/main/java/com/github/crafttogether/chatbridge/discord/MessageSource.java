package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;

public enum MessageSource {
    MINECRAFT(ChatBridge.plugin.getConfig().getConfigurationSection("discord").getString("minecraftPrefix")),
    DISCORD(""),
    IRC("");

    public final String icon;
    MessageSource(String icon) {
        this.icon = icon;
    }
}
