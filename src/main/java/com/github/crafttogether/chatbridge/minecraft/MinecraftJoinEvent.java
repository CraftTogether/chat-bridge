package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import discord4j.common.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

import static com.github.crafttogether.chatbridge.discord.DiscordBot.client;

public class MinecraftJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.GREEN)
                .title(String.format("%s has joined the server", event.getPlayer().getName()))
                .build();
        client.getChannelById(Snowflake.of(ChatBridge.plugin.getConfig().getConfigurationSection("discord").getLong("discordChannelId")))
                .createMessage(embed.asRequest())
                .block();
        try {
            IrcMessageSender.send(String.format("\u00033%s has joined the server", event.getPlayer().getName()));
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
