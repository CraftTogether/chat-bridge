package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.discord.DiscordMessageSender;
import com.github.crafttogether.chatbridge.MessageSource;
import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class MinecraftMessageListener implements Listener {

    @EventHandler
    public void onMessage(AsyncChatEvent event) {
        final String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        final String username = PlainTextComponentSerializer.plainText().serialize(event.getPlayer().displayName());
        final String avatar = "https://crafatar.com/avatars/" + event.getPlayer().getUniqueId();

        DiscordMessageSender.send(username, message, avatar, MessageSource.MINECRAFT);
        try {
            IrcMessageSender.send(String.format("[Minecraft]: <%s> %s", username, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}