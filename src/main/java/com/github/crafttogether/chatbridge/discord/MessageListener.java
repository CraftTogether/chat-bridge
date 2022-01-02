package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.MessageSource;
import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import com.github.crafttogether.chatbridge.minecraft.MinecraftMessageSender;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MessageListener {

    public void onGuildMessageReceived(@NotNull MessageCreateEvent event) {
        if (event.getMessage().getWebhookId().isPresent()) return;
        final String channelId = ChatBridge.plugin.getConfig().getConfigurationSection("discord").getString("discordChannelId");
        if (!channelId.equals(event.getMessage().getRestChannel().getId().asString())) return;

        MinecraftMessageSender.send(event.getMember().get().getDisplayName(), event.getMessage().getContent(), MessageSource.DISCORD);
        try {
            IrcMessageSender.send(String.format("[Discord]: <%s> %s", event.getMember().get().getDisplayName(), event.getMessage().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}