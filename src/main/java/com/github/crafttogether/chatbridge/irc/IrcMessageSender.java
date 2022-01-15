package com.github.crafttogether.chatbridge.irc;

import com.github.crafttogether.chatbridge.ChatBridge;

import java.io.IOException;

public class IrcMessageSender {
    private static String channel;

    public static void setChannel(String channel) {
        channel = channel;
    }

    public static void send(String message) throws IOException {
        if (!ChatBridge.isIrcConnected()) return;
        ChatBridge.getIrcThread().getClient().command.sendMessage(channel, message);
    }
}
