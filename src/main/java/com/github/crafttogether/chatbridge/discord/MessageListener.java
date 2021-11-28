package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.minecraft.MinecraftMessageHandler;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

public class MessageListener {

    public void onGuildMessageReceived(@NotNull MessageCreateEvent event) {
        if (event.getMessage().getWebhookId().isPresent()) return;
        final String channelId = ChatBridge.plugin.getConfig().getConfigurationSection("discord").getString("discordChannelId");
        if (!channelId.equals(event.getMessage().getRestChannel().getId().asString())) return;

        MinecraftMessageHandler.handle(event.getMember().get().getDisplayName(), event.getMessage().getContent());
        //IrcMessageHandler.handle()
    }
}