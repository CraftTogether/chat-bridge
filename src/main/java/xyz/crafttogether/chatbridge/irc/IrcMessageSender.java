package xyz.crafttogether.chatbridge.irc;

import xyz.crafttogether.chatbridge.ChatBridge;
import xyz.crafttogether.chatbridge.configuration.ConfigHandler;

import java.io.IOException;

public class IrcMessageSender {

    public static void send(String message) throws IOException {
        if (!ChatBridge.getIrcThread().getClient().isConnected()) return;
        ChatBridge.getIrcThread().getClient().getCommands().sendMessage(ConfigHandler.getConfig().getIrcConfigSection().getChannel(), message);
    }
}
