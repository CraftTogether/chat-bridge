package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.Bridge;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MinecraftMessageSender {

    public static void send(String author, String message, Bridge bridge) {
        TextComponent msg;
        if (bridge == Bridge.DISCORD) {
            msg = PlainTextComponentSerializer.plainText().deserialize(ChatColor.BLUE + String.format("[Discord]: <%s> %s", author, message));
        } else {
            msg = PlainTextComponentSerializer.plainText().deserialize(ChatColor.BLUE + String.format("[IRC]: <%s> %s", author, message));
        }
        Bukkit.getServer().sendMessage(msg);
    }

}