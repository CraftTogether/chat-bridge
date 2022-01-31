package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.events.PrivMessageEvent;
import dev.polarian.ircj.objects.messages.PrivMessage;
import xyz.crafttogether.chatbridge.MessageSource;
import xyz.crafttogether.chatbridge.discord.DiscordMessageSender;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftMessageSender;


public class OnPrivMessage implements PrivMessageEvent {
    @Override
    public void invoke(PrivMessage message) {
        MinecraftMessageSender.send(message.getNick(), message.getMessage(), MessageSource.IRC);
        DiscordMessageSender.send(message.getNick(), message.getMessage(), null, MessageSource.IRC);
    }
}
