package xyz.crafttogether.chatbridge.discord;

import dev.polarian.ircj.utils.Formatting;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.crafttogether.chatbridge.MessageSource;
import xyz.crafttogether.chatbridge.irc.IrcMessageSender;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftMessageSender;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;

import java.io.IOException;

/**
 * Handles discord event listening
 */
public class DiscordListener extends ListenerAdapter {
    /**
     * Invoked when a message is received in a channel the bot can read messages in
     * @param event The MessageReceivedEvent object
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().isWebhookMessage() || event.getMessage().getAuthor().isBot()) return;
        final long channelId = ConfigHandler.getConfig().getDiscordChannelId();
        if (channelId != event.getChannel().getIdLong()) return;
        Message referencedMessage = event.getMessage().getReferencedMessage();
        try {
            if (referencedMessage != null) {
                MinecraftMessageSender.send(referencedMessage.getAuthor().getName(), referencedMessage.getContentRaw(), MessageSource.DISCORD_REFERENCE);
                IrcMessageSender.send(String.format("(referenced) %s[Discord]: <%s%s> %s",
                        Formatting.COLOUR_GRAY, Formatting.ZWSP, referencedMessage.getAuthor().getName(),
                        referencedMessage.getContentRaw()));
            }
            MinecraftMessageSender.send(event.getMember().getEffectiveName(), event.getMessage().getContentRaw(), MessageSource.DISCORD);
            IrcMessageSender.send(String.format("%s[Discord]: <%s%s> %s",
                    Formatting.COLOUR_CYAN, Formatting.ZWSP,
                    event.getMember().getEffectiveName(), event.getMessage().getContentRaw()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}