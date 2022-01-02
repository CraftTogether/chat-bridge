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
        try {
            new Webhook(ChatBridge.plugin.getConfig().getConfigurationSection("discord").getString("webhook"))
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
