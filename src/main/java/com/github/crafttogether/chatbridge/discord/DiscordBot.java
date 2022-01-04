package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.bukkit.Bukkit;

public class DiscordBot {
    public static DiscordClient client;

    public static void start() {
        Bukkit.getConsoleSender().sendMessage("Connecting to Discord");

        final String token = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("token");
        client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();

        final MessageListener listener = new MessageListener();
        gateway.on(MessageCreateEvent.class).subscribe(listener::onGuildMessageReceived);
    }

}
