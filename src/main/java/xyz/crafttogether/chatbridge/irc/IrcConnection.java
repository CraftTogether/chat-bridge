package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.IrcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IrcConnection extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(IrcConnection.class);
    private final IrcClient client;

    public IrcConnection(IrcClient client) {
        this.setName("IRC-Connection");
        this.client = client;
    }

    public IrcClient getClient() {
        return client;
    }

    @Override
    public void run() {
        logger.info("IRC thread started");
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
