package xyz.crafttogether.chatbridge.minecraft.listeners;

import dev.polarian.ircj.utils.Formatting;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import xyz.crafttogether.craftcore.CraftCore;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;
import xyz.crafttogether.craftcore.connector.AccountConnection;
import xyz.crafttogether.craftcore.connector.AccountConnector;
import xyz.crafttogether.weg.Weg;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

/**
 * Event listener which handles when a player joins the minecraft server
 */
public class MinecraftJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        assignColor(event.getPlayer());
        ChatBridge.updateIrcChannelStatistics(Bukkit.getOnlinePlayers().size(), Weg.getAfkPlayers());

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(String.format("%s has joined the server", event.getPlayer().getName()));
        final long channelId = ConfigHandler.getConfig().getDiscordChannelId();
        CraftCore.getJda().getTextChannelById(String.valueOf(channelId)).sendMessageEmbeds(embed.build()).queue();
        try {
            IrcMessageSender.send(String.format("%s%s%s has joined the server",
                    Formatting.COLOUR_GREEN, Formatting.ZWSP, event.getPlayer().getName()));
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private void assignColor(Player player) {
        Optional<AccountConnection> optional = AccountConnector.getAccount(player.getUniqueId());
        if (optional.isEmpty()) return;
        AccountConnection account = optional.get();
        final long guildId = ConfigHandler.getConfig().getDiscordGuildId();

        final Guild guild = CraftCore.getJda().getGuildById(guildId);
        guild.retrieveMemberById(account.getDiscordId()).queue(member -> {
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
