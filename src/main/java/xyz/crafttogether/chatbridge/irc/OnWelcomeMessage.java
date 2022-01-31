package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.events.WelcomeEvent;
import dev.polarian.ircj.objects.messages.WelcomeMessage;
import xyz.crafttogether.chatbridge.ChatBridge;

public class OnWelcomeMessage implements WelcomeEvent {
    @Override
    public void invoke(WelcomeMessage message) {
        ChatBridge.setIrcConnected(true);
        ChatBridge.resetAttempts();
    }
}
