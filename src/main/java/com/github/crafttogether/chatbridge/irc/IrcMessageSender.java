package com.github.crafttogether.chatbridge.irc;

import dev.polarian.ircj.IrcClient;

import java.io.IOException;

public class IrcMessageSender {
    public static IrcClient client;
    public static String channel;

    public static void send(String message) throws IOException {
        client.command.sendMessage(channel, message);
    }
}
