package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.DisconnectReason;
import dev.polarian.ircj.events.DisconnectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.chatbridge.ChatBridge;

public class OnDisconnect implements DisconnectEvent {
    private static final Logger logger = LoggerFactory.getLogger(OnDisconnect.class);

    @Override
    public void invoke(DisconnectReason reason, Exception e) {
        logger.warn("Disconnected from IRC server for reason: " + reason.toString().toLowerCase());
        ChatBridge.setIrcConnected(false);
        switch (reason) {
            case TIMEOUT:
                if (ChatBridge.getRemainingAttempts() > 0) ChatBridge.createIrcConnection();
                ChatBridge.decrementRemainingAttempts();
                break;

            case FORCE_DISCONNECTED:
                return;

            case ERROR:
                logger.error(e.getMessage());
        }
    }
}
