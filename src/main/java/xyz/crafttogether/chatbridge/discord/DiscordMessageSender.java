package xyz.crafttogether.chatbridge.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.chatbridge.MessageSource;
import xyz.crafttogether.chatbridge.utilities.Webhook;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;

import java.io.IOException;

/**
 * Class which abstracts the forwarding of messages to discord based on the message source
 */
public class DiscordMessageSender {

    /**
     * SLF4J instance
     */
    private static final Logger logger = LoggerFactory.getLogger(DiscordMessageSender.class);

    /**
     * Sends a message to discord using the webhook provided in the plugin configuration
     *
     * @param username The username of the user which sent the message
     * @param message The message content
     * @param avatar The avatar of the user (for example the minecraft avatar)
     * @param source The message source
     */
    public static void send(String username, String message, String avatar, MessageSource source) {
        // check if the message pings @everyone or @here
        String[] args = message.split("\s+");
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("@everyone") || args[i].startsWith("@here")) {
                args[i] = "";
            }
        }
        message = String.join(" ", args);
        if (message.length() == 0) return;
        try {
            new Webhook(ConfigHandler.getConfig().getDiscordWebhook())
                    .setAvatarUrl(avatar)
                    .setUsername(username)
                    .setContent(source.icon + " " + message)
                    .send();
        } catch (IOException e) {
            logger.info("Failed to send message to discord:");
            e.printStackTrace();
        }
    }

}
