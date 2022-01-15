package com.github.crafttogether.chatbridge.irc;

import com.github.crafttogether.chatbridge.ChatBridge;
import dev.polarian.ircj.events.WelcomeEvent;
import dev.polarian.ircj.objects.messages.WelcomeMessage;

public class OnWelcomeMessage implements WelcomeEvent {
    @Override
    public void invoke(WelcomeMessage message) {
        ChatBridge.setIrcConnected(true);
        ChatBridge.resetAttempts();
    }
}
