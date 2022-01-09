package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;
import java.io.IOException;

import static com.github.crafttogether.chatbridge.discord.DiscordBot.client;

public class MinecraftJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(String.format("%s has joined the server", event.getPlayer().getName()));
        final long channelId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getLong("discordChannelId");
        client.getTextChannelById(String.valueOf(channelId)).sendMessageEmbeds(embed.build()).queue();
        try {
            IrcMessageSender.send(String.format("\u00033%s has joined the server", event.getPlayer().getName()));
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
