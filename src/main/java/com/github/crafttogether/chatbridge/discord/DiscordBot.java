package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;

public class DiscordBot {

    public static void start() {
        final DiscordClient client = DiscordClient.create(ChatBridge.plugin.getConfig().getString("token"));
        final GatewayDiscordClient gateway = client.login().block();

        final MessageListener listener = new MessageListener();

        gateway.on(MessageCreateEvent.class).subscribe(listener::onGuildMessageReceived);
    }

}
