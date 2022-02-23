package xyz.crafttogether.chatbridge.minecraft.listeners;

import dev.polarian.ircj.utils.Formatting;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.crafttogether.chatbridge.MessageSource;
import xyz.crafttogether.chatbridge.discord.DiscordMessageSender;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;

import java.io.IOException;

/**
 * Event listener which handles the messages' people send on the minecraft server
 */
public class MinecraftMessageListener implements Listener {

    @EventHandler
    public void onMessage(AsyncChatEvent event) {
        final String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        final String username = PlainTextComponentSerializer.plainText().serialize(event.getPlayer().displayName());
        final String avatar = "https://crafatar.com/avatars/" + event.getPlayer().getUniqueId();

        DiscordMessageSender.send(username, message, avatar, MessageSource.MINECRAFT);
        try {
            IrcMessageSender.send(username, message, MessageSource.MINECRAFT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}