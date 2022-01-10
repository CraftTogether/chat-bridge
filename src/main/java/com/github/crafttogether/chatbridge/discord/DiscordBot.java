package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private static final Logger logger = LoggerFactory.getLogger(DiscordBot.class);
    public static JDA client;

    public static void start() throws LoginException {
        Bukkit.getConsoleSender().sendMessage("Connecting to Discord");

        final String token = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("token");
        client = JDABuilder.createLight(token)
                .setEnabledIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new MessageListener(), new LinkCommand())
                .build();
        try {
            client.awaitReady();
            logger.info("Successfully connected to discord");
            ChatBridge.setDiscordConnected(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
