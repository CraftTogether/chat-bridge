package xyz.crafttogether.chatbridge.minecraft.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.configuration.ConfigHandler;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import xyz.crafttogether.craftcore.CraftCore;
import xyz.crafttogether.weg.Weg;

import java.awt.*;
import java.io.IOException;

/**
 * Event listener which handles when a player leaves the minecraft server
 */
public class MinecraftQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ChatBridge.updateChannelStatistics(Bukkit.getOnlinePlayers().size(), Weg.getAfkPlayers());
        final EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(String.format("%s has left the server", event.getPlayer().getName()));
        final long channelId = ConfigHandler.getConfig().getDiscordConfigSection().getChannelId();
        CraftCore.getJda().getTextChannelById(String.valueOf(channelId)).sendMessageEmbeds(embed.build()).queue();
        try {
            IrcMessageSender.send(String.format("\u00034%s has left the server", event.getPlayer().getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
