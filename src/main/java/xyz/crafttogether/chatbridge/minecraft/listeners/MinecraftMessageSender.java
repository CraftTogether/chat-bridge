package xyz.crafttogether.chatbridge.minecraft.listeners;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.crafttogether.chatbridge.MessageSource;

/**
 * Class containing a static method to abstract the forwarding of messages to minecraft based on the message source
 */
public class MinecraftMessageSender {

    public static void send(String author, String message, MessageSource source) {
        TextComponent msg = null;
        switch (source) {
            case DISCORD -> {
                msg = PlainTextComponentSerializer.plainText().deserialize(String.format("%s[Discord]: <%s> %s",
                        ChatColor.AQUA, author, message));
            }
            case DISCORD_REFERENCE -> {
                msg = PlainTextComponentSerializer.plainText().deserialize(String.format("%s(referenced) [Discord]: <%s> %s",
                        ChatColor.AQUA, author, message));
            }
            case IRC -> {
                msg = PlainTextComponentSerializer.plainText().deserialize(String.format("[IRC]: <%s> %s", author, message));
            }
        }
        if (msg == null) return;
        Bukkit.getServer().sendMessage(msg);
    }

}