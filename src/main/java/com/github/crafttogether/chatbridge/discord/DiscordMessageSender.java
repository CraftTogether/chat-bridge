package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.utilities.Webhook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.IOException;

public class DiscordMessageSender {

    public static void handle(String username, String message, String avatar, MessageSource source) {
        try {
            new Webhook("https://canary.discord.com/api/webhooks/913818035930931241/yb5JfGwN2cLbfZaBLWOIxmPxWsHSgKB2RLRpAGo1KOITdqNhJqcwB7kR6nmvomw-BCYY")
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
