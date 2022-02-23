package xyz.crafttogether.chatbridge.minecraft.listeners;

import dev.polarian.ircj.utils.Formatting;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import xyz.crafttogether.craftcore.CraftCore;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;
import xyz.crafttogether.weg.Weg;

import java.awt.*;
import java.io.IOException;

/**
 * Event listener which handles when a player leaves the minecraft server
 */
public class MinecraftQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ChatBridge.updateIrcChannelStatistics(Bukkit.getOnlinePlayers().size(), Weg.getAfkPlayers());
        final EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(String.format("%s has left the server", event.getPlayer().getName()));
        final long channelId = ConfigHandler.getConfig().getDiscordChannelId();
        CraftCore.getJda().getTextChannelById(String.valueOf(channelId)).sendMessageEmbeds(embed.build()).queue();
        try {
            IrcMessageSender.send(String.format("%s%s%s has left the server",
                    Formatting.COLOUR_RED, Formatting.ZWSP, event.getPlayer().getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
