package xyz.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.kelp.Kelp;
import com.github.crafttogether.weg.events.AfkEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;

import java.awt.*;
import java.io.IOException;

public class AfkListener implements AfkEvent {
    private static final Logger logger = LoggerFactory.getLogger(AfkListener.class);

    @Override
    public void invoke(Player player) {
         MessageEmbed embed = new EmbedBuilder()
                .setTitle(player.getName() + " has went afk")
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
}
