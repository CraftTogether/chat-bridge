package com.github.crafttogether.chatbridge.irc;

import com.github.crafttogether.chatbridge.ChatBridge;
import dev.polarian.ircj.events.DisconnectEvent;

public class OnDisconnect implements DisconnectEvent {
    @Override
    public void invoke() {
        ChatBridge.setIrcConnected(false);
    }
}
