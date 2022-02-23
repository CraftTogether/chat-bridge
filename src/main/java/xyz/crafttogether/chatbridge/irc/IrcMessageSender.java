package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.utils.Formatting;
import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.MessageSource;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;

import java.io.IOException;

/**
 * A class which abstracts the forwarding of messages to the IRC channel depending on the message source
 */
public class IrcMessageSender {

    public static void send(String username, String messageContent, MessageSource source) throws IOException {
        if (!ChatBridge.getIrcClient().isConnected()) return;
        String message = null;
        switch (source) {
            case DISCORD -> {
                message = String.format("%s[Discord]: <%s> %s",
                        Formatting.COLOUR_CYAN, parseUsername(username), messageContent);
            }

            case DISCORD_REFERENCE -> {
                message = String.format("%s(referenced) [Discord]: <%s> %s",
                        Formatting.COLOUR_CYAN, parseUsername(username), messageContent);
            }

            case MINECRAFT -> {
                message = String.format("%s[Minecraft]: <%s> %s",
                        Formatting.COLOUR_CYAN, parseUsername(username), messageContent);
            }
        }
        if (message == null) return;
        ChatBridge.getIrcClient().getCommands().sendMessage(ConfigHandler.getConfig().getIrcChannel(), message);
    }

    public static void sendMinecraftQuitMessage(String username) throws IOException {
        ChatBridge.getIrcClient().getCommands().sendMessage(ConfigHandler.getConfig().getIrcChannel(),
                String.format("%s%s has left the server", Formatting.COLOUR_RED, parseUsername(username)));
    }

    public static void sendMinecraftJoinMessage(String username) throws IOException {
        ChatBridge.getIrcClient().getCommands().sendMessage(ConfigHandler.getConfig().getIrcChannel(),
                String.format("%s%s has joined the server", Formatting.COLOUR_GREEN, parseUsername(username)));
    }

    public static void sendMinecraftAfkMessage(String username) throws IOException {
        ChatBridge.getIrcClient().getCommands().sendMessage(ConfigHandler.getConfig().getIrcChannel(),
                String.format("%s%s has went AFK", Formatting.COLOUR_GRAY, parseUsername(username)));
    }

    public static void sendMinecraftReturnMessage(String username) throws IOException {
        ChatBridge.getIrcClient().getCommands().sendMessage(ConfigHandler.getConfig().getIrcChannel(),
                String.format("%s%s is no longer AFK", Formatting.COLOUR_GRAY, parseUsername(username)));
    }

    private static String parseUsername(String username) {
        int midIndex = (int) username.length() / 2;
        return String.format("%s%s%s", username.substring(0, midIndex), Formatting.ZWSP, username.substring(midIndex));
    }
}
