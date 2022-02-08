package xyz.crafttogether.chatbridge.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.MessageSource;
import xyz.crafttogether.chatbridge.configuration.ConfigHandler;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftMessageSender;

import java.io.IOException;

/**
 * Handles discord event listening
 */
public class DiscordListener extends ListenerAdapter {

    /**
     * Invoked when A slash command is executed
     * @param event The SlashCommandEvent object
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Command command = ChatBridge.getDiscordCommand(event.getName());
        if (command == null) {
            event.reply("Invalid command").queue();
        } else {
            command.invoke(event);
        }
    }

    /**
     * Invoked when a message is received in a channel the bot can read messages in
     * @param event The MessageReceivedEvent object
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().isWebhookMessage()) return;
        if (event.getMessage().getAuthor().isBot()) return;
        final long channelId = ConfigHandler.getConfig().getDiscordConfigSection().getChannelId();
        if (channelId != event.getChannel().getIdLong()) return;

        MinecraftMessageSender.send(event.getMember().getEffectiveName(), event.getMessage().getContentRaw(), MessageSource.DISCORD);
        try {
            IrcMessageSender.send(String.format("\u000310[Discord]: <\u200B%s> %s", event.getMember().getEffectiveName(), event.getMessage().getContentRaw()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}