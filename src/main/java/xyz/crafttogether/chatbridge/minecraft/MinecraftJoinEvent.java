package xyz.crafttogether.chatbridge.minecraft;

import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import com.github.crafttogether.kelp.Kelp;
import com.github.crafttogether.rinku.Connection;
import com.github.crafttogether.rinku.Rinku;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        final Connection connection = Rinku.find(c -> c.getMinecraft().equals(player.getUniqueId().toString()));
        if (connection != null) {
            final String guildId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("guildId");
            if (guildId == null) return;

            final Guild guild = Kelp.getClient().getGuildById(guildId);
            guild.retrieveMemberById(connection.getDiscord()).queue(member -> {
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
