package com.github.crafttogether.chatbridge.discord;

import com.github.crafttogether.chatbridge.ChatBridge;
import com.github.crafttogether.chatbridge.MessageSource;
import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import com.github.crafttogether.chatbridge.minecraft.MinecraftMessageSender;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;

public class MessageListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().isWebhookMessage()) return;
        if (event.getMessage().getAuthor().isBot()) return;
        final String channelId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("discordChannelId");
        if (!channelId.equals(event.getChannel().getId())) return;

        MinecraftMessageSender.send(event.getMember().getEffectiveName(), event.getMessage().getContentRaw(), MessageSource.DISCORD);
        try {
            IrcMessageSender.send(String.format("\u000310[Discord]: <%s> %s", event.getMember().getEffectiveName(), event.getMessage().getContentRaw()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}