package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    public static JDA client;

    public static void start() throws LoginException {
        Bukkit.getConsoleSender().sendMessage("Connecting to Discord");

        final String token = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("token");
        client = JDABuilder.createLight(token)
                .setEnabledIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new MessageListener(), new LinkCommand())
                .build();
    }

}
