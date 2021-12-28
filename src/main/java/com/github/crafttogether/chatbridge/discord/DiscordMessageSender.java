package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.utilities.Webhook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.IOException;

public class DiscordMessageSender {

    public static void send(String username, String message, String avatar, MessageSource source) {
        try {
            new Webhook(ChatBridge.plugin.getConfig().getConfigurationSection("discord").getString("webhook"))
                    .setAvatarUrl(avatar)
                    .setUsername(username)
                    .setContent(source.icon + " " + message)
                    .send();
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error while sending a message to Discord");
            e.printStackTrace();
        }
    }

}
