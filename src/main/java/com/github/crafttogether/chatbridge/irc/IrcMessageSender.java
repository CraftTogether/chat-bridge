package com.github.crafttogether.chatbridge.irc;

import com.github.crafttogether.chatbridge.ChatBridge;

import java.io.IOException;

public class IrcMessageSender {

    public static void send(String message) throws IOException {
        if (!ChatBridge.isIrcConnected()) return;
        ChatBridge.getIrcThread().getClient().getCommands().sendMessage(ChatBridge.getChannel(), message);
    }
}
