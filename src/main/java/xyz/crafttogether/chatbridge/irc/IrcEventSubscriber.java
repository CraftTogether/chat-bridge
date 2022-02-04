package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.DisconnectReason;
import dev.polarian.ircj.EventListener;
import dev.polarian.ircj.objects.messages.PrivMessage;
import dev.polarian.ircj.objects.messages.WelcomeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.MessageSource;
import xyz.crafttogether.chatbridge.configuration.ConfigHandler;
import xyz.crafttogether.chatbridge.discord.DiscordMessageSender;
import xyz.crafttogether.chatbridge.minecraft.listeners.MinecraftMessageSender;

public class IrcEventSubscriber extends EventListener {
    private static final Logger logger = LoggerFactory.getLogger(IrcEventSubscriber.class);

    @Override
    public void onDisconnectEvent(DisconnectReason reason, Exception e) {
        logger.warn("Disconnected from IRC server for reason: " + reason.toString().toLowerCase());
        switch (reason) {
            case TIMEOUT:
                if (ChatBridge.getRemainingAttempts() > 0) ChatBridge.createIrcConnection();
                ChatBridge.decrementRemainingAttempts();
                break;

            case FORCE_DISCONNECTED:
                return;

            case ERROR:
                logger.error(e.getMessage());
        }
    }

    @Override
    public void onPrivMessageEvent(PrivMessage message) {
        String prefix = ConfigHandler.getConfig().getIrcConfigSection().getCommandPrefix();
        if (message.getMessage().startsWith(prefix)) {
            CommandHandler.parseCommand(message, prefix);
        }
        MinecraftMessageSender.send(message.getNick(), message.getMessage(), MessageSource.IRC);
        DiscordMessageSender.send(message.getNick(), message.getMessage(), null, MessageSource.IRC);
    }

    @Override
    public void onWelcomeEvent(WelcomeMessage message) {
        ChatBridge.resetAttempts();
    }
}
