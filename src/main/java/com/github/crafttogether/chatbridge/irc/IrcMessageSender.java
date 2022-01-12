package com.github.crafttogether.chatbridge.irc;

import com.github.crafttogether.chatbridge.ChatBridge;
import dev.polarian.ircj.IrcClient;

import java.io.IOException;

public class IrcMessageSender {
    public static IrcClient client;
    public static String channel;

    public static void send(String message) throws IOException {
        if (!ChatBridge.isIrcConnected()) return;
        client.command.sendMessage(channel, message);
    }
}
