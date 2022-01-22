package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import com.github.crafttogether.chatbridge.utilities.Members;
import com.github.crafttogether.kelp.Kelp;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class MinecraftJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Kelp.isConnected()) return;
        assignColor(event.getPlayer());

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(String.format("%s has joined the server", event.getPlayer().getName()));
        final long channelId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getLong("discordChannelId");
        Kelp.getClient().getTextChannelById(String.valueOf(channelId)).sendMessageEmbeds(embed.build()).queue();
        try {
            IrcMessageSender.send(String.format("\u00033%s has joined the server", event.getPlayer().getName()));
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private void assignColor(Player player) {
        final JSONArray members = Members.get();
        for (int i = 0; i < members.length(); i++) {
            final JSONObject data = members.getJSONObject(i);

            final String discordId = data.getString("discord");
            final String minecraftUuid = data.getString("minecraft");

            if (minecraftUuid.equals(player.getUniqueId().toString())) {
                final String guildId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("guildId");
                if (guildId == null) return;

                final Guild guild = Kelp.getClient().getGuildById(guildId);
                guild.retrieveMemberById(discordId).queue(member -> {
                    final TextComponent text = PlainTextComponentSerializer
                            .plainText()
                            .deserialize(player.getName());

                    final Color colour = member.getColor();
                    if (colour != null) {
                        final int r = colour.getRed();
                        final int g = colour.getGreen();
                        final int b = colour.getBlue();

                        text.color(TextColor.color(r, g, b));
                    }

                    player.playerListName(text);
                });
            }
        }
    }

}
