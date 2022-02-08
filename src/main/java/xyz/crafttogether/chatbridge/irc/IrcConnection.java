package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.IrcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Extends Thread class, handles the IRC connection
 */
public class IrcConnection extends Thread {
    /**
     * SLF4J Logger instance
     */
    private static final Logger logger = LoggerFactory.getLogger(IrcConnection.class);
    /**
     * IrcClient instance
     */
    private final IrcClient client;

    /**
     * IrcConnection constructor, sets the IrcClient instance (constant), and the name of the Thread
     *
     * @param client
     */
    public IrcConnection(IrcClient client) {
        this.setName("IRC-Connection");
        this.client = client;
    }

    /**
     * Get the IRC client object
     *
     * @return The IRC Client object
     */
    public IrcClient getClient() {
        return client;
    }

    /**
     * runs the thread
     */
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
