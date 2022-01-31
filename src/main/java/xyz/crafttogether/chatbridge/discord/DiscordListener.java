package xyz.crafttogether.chatbridge.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.MessageSource;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftMessageSender;

import java.io.IOException;

public class DiscordListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Command command = ChatBridge.getDiscordCommand(event.getName());
        if (command == null) {
            event.reply("Invalid command").queue();
        } else {
            command.invoke(event);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().isWebhookMessage()) return;
        if (event.getMessage().getAuthor().isBot()) return;
        final String channelId = ChatBridge.getPlugin().getConfig().getConfigurationSection("discord").getString("discordChannelId");
        if (!channelId.equals(event.getChannel().getId())) return;

        MinecraftMessageSender.send(event.getMember().getEffectiveName(), event.getMessage().getContentRaw(), MessageSource.DISCORD);
        try {
            IrcMessageSender.send(String.format("\u000310[Discord]: <\u200B%s> %s", event.getMember().getEffectiveName(), event.getMessage().getContentRaw()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}