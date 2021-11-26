package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.discord.DiscordMessageHandler;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MinecraftMessageListener implements Listener {

    @EventHandler
    public void onMessage(AsyncChatEvent event) {
        final String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        final String username = PlainTextComponentSerializer.plainText().serialize(event.getPlayer().displayName());
        final String avatar = "https://crafatar.com/avatars/" + event.getPlayer().getUniqueId();

        Bukkit.getConsoleSender().sendMessage("Recieved message");
        handle(message, username, avatar);
    }

    public void handle(String username, String message, String avatar) {
        DiscordMessageHandler.handle(username, message, avatar);
    }

}