package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    public static void start() throws LoginException {
        JDABuilder
                .createLight(ChatBridge.plugin.getConfig().getString("token"))
                .setEnabledIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new MessageListener())
                .build();
    }

}
