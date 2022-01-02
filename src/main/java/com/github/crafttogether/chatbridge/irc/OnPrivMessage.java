package com.github.crafttogether.chatbridge.irc;

import com.github.crafttogether.chatbridge.discord.DiscordMessageSender;
import com.github.crafttogether.chatbridge.MessageSource;
import com.github.crafttogether.chatbridge.minecraft.MinecraftMessageSender;
import dev.polarian.ircj.events.PrivMessageEvent;
import dev.polarian.ircj.objects.messages.PrivMessage;


public class OnPrivMessage implements PrivMessageEvent {
    @Override
    public void invoke(PrivMessage message) {
        MinecraftMessageSender.send(message.getNick(), message.getMessage(), MessageSource.IRC);
        DiscordMessageSender.send(message.getNick(), message.getMessage(), null, MessageSource.IRC);
    }
}
