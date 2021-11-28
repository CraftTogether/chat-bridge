package com.github.crafttogether.chatbridge.irc;

import dev.polarian.ircj.events.WelcomeEvent;
import dev.polarian.ircj.objects.messages.WelcomeMessage;

import static com.github.crafttogether.chatbridge.ChatBridge.ircConnected;

public class OnWelcomeMessage implements WelcomeEvent {
    @Override
    public void invoke(WelcomeMessage message) {
        ircConnected = true;
    }
}
