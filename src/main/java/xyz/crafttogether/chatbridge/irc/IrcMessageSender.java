package xyz.crafttogether.chatbridge.irc;

import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.configuration.ConfigHandler;

import java.io.IOException;

/**
 * A class which abstracts the forwarding of messages to the IRC channel depending on the message source
 */
public class IrcMessageSender {

    public static void send(String message) throws IOException {
        if (!ChatBridge.getIrcThread().getClient().isConnected()) return;
        ChatBridge.getIrcThread().getClient().getCommands().sendMessage(ConfigHandler.getConfig().getIrcConfigSection().getChannel(), message);
    }
}
