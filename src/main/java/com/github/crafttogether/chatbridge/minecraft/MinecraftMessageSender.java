package com.github.crafttogether.chatbridge.minecraft;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;

public class MinecraftMessageSender {

    public static void send(String author, String message) {
        TextComponent msg = PlainTextComponentSerializer.plainText().deserialize(String.format("<%s> %s", author, message));
        Bukkit.getServer().sendMessage(msg);
    }

}