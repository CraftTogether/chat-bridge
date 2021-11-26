package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

public class MessageListener {

    public void onGuildMessageReceived(@NotNull MessageCreateEvent event) {
        if (event.getMessage().getWebhookId().isPresent()) return;
        if (!ChatBridge.plugin.getConfig().getString("discordChannelId").equals(event.getMessage().getRestChannel().getId().asString())) return;

        //MinecraftMessageHandler.handle()
        //IrcMessageHandler.handle()
    }
}