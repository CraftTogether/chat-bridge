package com.github.crafttogether.chatbridge.irc;

import dev.polarian.ircj.events.PrivMessageEvent;
import dev.polarian.ircj.objects.messages.PrivMessage;

public class OnPrivMessage implements PrivMessageEvent {
    @Override
    public void invoke(PrivMessage message) {
        // handle message logic here
    }
}
