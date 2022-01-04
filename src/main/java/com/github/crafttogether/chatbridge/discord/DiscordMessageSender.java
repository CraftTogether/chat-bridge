package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.MessageSource;
import com.github.crafttogether.chatbridge.utilities.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DiscordMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(DiscordMessageSender.class);

    public static void send(String username, String message, String avatar, MessageSource source) {
        // check if the message pings @everyone or @here
        String[] args = message.split("\s+");
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("@everyone") || args[i].startsWith("@here")) {
                args[i] = "";
            }
        }
        message = String.join(" ", args);
        if (message.length() == 0) return;
        try {
            new Webhook(ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("webhook"))
                    .setAvatarUrl(avatar)
                    .setUsername(username)
                    .setContent(source.icon + " " + message)
                    .send();
        } catch (IOException e) {
            logger.info("Failed to send message to discord:");
            e.printStackTrace();
        }
    }

}
