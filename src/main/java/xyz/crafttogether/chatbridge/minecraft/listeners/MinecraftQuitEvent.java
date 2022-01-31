package xyz.crafttogether.chatbridge.minecraft.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import xyz.crafttogether.kelp.Kelp;

import java.awt.*;
import java.io.IOException;

public class MinecraftQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!Kelp.isConnected()) return;
        final EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(String.format("%s has left the server", event.getPlayer().getName()));
        final long channelId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getLong("discordChannelId");
        Kelp.getClient().getTextChannelById(String.valueOf(channelId)).sendMessageEmbeds(embed.build()).queue();
        try {
            IrcMessageSender.send(String.format("\u00034%s has left the server", event.getPlayer().getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
