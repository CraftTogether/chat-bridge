package xyz.crafttogether.chatbridge.minecraft.listeners;

import com.github.crafttogether.kelp.Kelp;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import xyz.crafttogether.weg.EventListener;

import java.awt.*;
import java.io.IOException;

public class WegListener extends EventListener {
    private static final Logger logger = LoggerFactory.getLogger(WegListener.class);

    @Override
    public void onAfkEvent(Player player) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(player.getName() + " has went AFK")
                .setColor(Color.GRAY)
                .build();
        long channelId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getLong("discordChannelId");
        TextChannel channel = Kelp.getClient().getTextChannelById(channelId);
        if (channel == null) {
            logger.error("Failed to get discord channel");
        }
        channel.sendMessageEmbeds(embed).queue();
        try {
            IrcMessageSender.send(String.format("\00314%s has went AFK", player.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReturnEvent(Player player) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(player.getName() + " is no longer AFK")
                .setColor(Color.GRAY)
                .build();
        long channelId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getLong("discordChannelId");
        TextChannel channel = Kelp.getClient().getTextChannelById(channelId);
        if (channel == null) {
            logger.error("Failed to get discord channel");
        }
        channel.sendMessageEmbeds(embed).queue();
        try {
            IrcMessageSender.send(String.format("\00314%s is no longer AFK", player.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
