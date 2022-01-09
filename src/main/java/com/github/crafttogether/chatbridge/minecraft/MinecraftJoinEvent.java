package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import com.github.crafttogether.chatbridge.utilities.Members;
import discord4j.common.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.MemberData;
import discord4j.rest.entity.RestGuild;
import discord4j.rest.util.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static com.github.crafttogether.chatbridge.discord.DiscordBot.client;

public class MinecraftJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.GREEN)
                .title(String.format("%s has joined the server", event.getPlayer().getName()))
                .build();
        client.getChannelById(Snowflake.of(ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getLong("discordChannelId")))
                .createMessage(embed.asRequest())
                .subscribe();
        try {
            IrcMessageSender.send(String.format("\u00033%s has joined the server", event.getPlayer().getName()));
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
