package xyz.crafttogether.chatbridge.minecraft.listeners;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.crafttogether.chatbridge.MessageSource;

public class MinecraftMessageSender {

    public static void send(String author, String message, MessageSource source) {
        TextComponent msg;
        if (source == MessageSource.DISCORD) {
            msg = PlainTextComponentSerializer.plainText().deserialize(ChatColor.AQUA + String.format("[Discord]: <%s> %s", author, message));
        } else {
            msg = PlainTextComponentSerializer.plainText().deserialize(ChatColor.AQUA + String.format("[IRC]: <%s> %s", author, message));
        }
        Bukkit.getServer().sendMessage(msg);
    }

}