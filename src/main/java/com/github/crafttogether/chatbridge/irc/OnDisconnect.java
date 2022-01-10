package com.github.crafttogether.chatbridge.irc;

import com.github.crafttogether.chatbridge.ChatBridge;
import dev.polarian.ircj.events.DisconnectEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class OnDisconnect implements DisconnectEvent {
    @Override
    public void invoke() {
        ChatBridge.setIrcConnected(false);
        Bukkit.getServer().sendMessage(PlainTextComponentSerializer.plainText().deserialize(ChatColor.RED + "[Chat Bridge]: Disconnected from IRC server!"));
    }
}
