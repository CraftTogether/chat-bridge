package xyz.crafttogether.chatbridge.configuration.sections;

import xyz.crafttogether.chatbridge.configuration.ConfigSection;

/**
 * Configuration section which stores the IRC configuration
 */
public class IrcConfigSection extends ConfigSection {
    /**
     * Whether IRC is enabled
     */
    private final boolean enabled;
    /**
     * The IRC username
     */
    private final String username;
    /**
     * The IRC server hostname
     */
    private final String hostname;
    /**
     * The IRC server port
     */
    private final int port;
    /**
     * Whether tls will be used to connect to the IRC server
     */
    private final boolean tls;
    /**
     * The IRC connection timeout
     */
    private final int timeout;
    /**
     * The IRC channel to bridge with discord and minecraft
     */
    private final String channel;
    /**
     * The IRC prefix
     */
    private final String prefix;
    /**
     * The number of attempts to reconnect to the IRC server until the connection is aborted
     */
    private final int reconnectAttempts;
    /**
     * The delay between reconnect attempts
     */
    private final int reconnectDelay;
    /**
     * The IRC command prefix
     */
    private final String commandPrefix;

    /**
     * Constructor to create irc configuration section
     *
     * @param enabled Whether IRC is enabled
     * @param username The IRC username
     * @param hostname The IRC server hostname
     * @param port The IRC server port
     * @param tls Whether TLS will be used to connect to the IRC server
     * @param timeout The amount of time until the connection times out
     * @param channel The IRC channel which will be bridged with the discord channel and minecraft server
     * @param prefix The IRC prefix
     * @param reconnectAttempts The number of attempts to reconnect to the IRC server until the connection is aborted
     * @param reconnectDelay The delay between connection attempts
     * @param commandPrefix The IRC command prefix
     */
    public IrcConfigSection(boolean enabled, String username, String hostname, int port, boolean tls, int timeout,
                            String channel, String prefix, int reconnectAttempts, int reconnectDelay,
                            String commandPrefix) {
        this.enabled = enabled;
        this.username = username;
        this.hostname = hostname;
        this.port = port;
        this.tls = tls;
        this.timeout = timeout;
        this.channel = channel;
        this.prefix = prefix;
        this.reconnectAttempts = reconnectAttempts;
        this.reconnectDelay = reconnectDelay;
        this.commandPrefix = commandPrefix;
    }

    /**
     * Get whether IRC Is enabled
     *
     * @return Whether IRC is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get the IRC username
     *
     * @return IRC username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the IRC server hostname or IP
     *
     * @return IRC server hostname or IP
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Get the IRC server port
     *
     * @return IRC server port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get whether TLS is enabled
     *
     * @return Whether IRC Is enabled
     */
    public boolean isTlsEnabled() {
        return tls;
    }

    /**
     * Get the connection timeout
     *
     * @return Connection timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Get the IRC channel which is bridged with the discord channel and minecraft server
     *
     * @return IRC channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Gets the IRC prefix which is appended to the beginning of messages sent to discord
     *
     * @return IRC prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the number of attempts to reconnect to IRC before aborting the connection
     *
     * @return Number of attempts
     */
    public int getReconnectAttempts() {
        return reconnectAttempts;
    }

    /**
     * Get the delay between reconnect attempts
     *
     * @return Delay between reconnect attempts
     */
    public int getReconnectDelay() {
        return reconnectDelay;
    }

    /**
     * Get the IRC command prefix
     *
     * @return IRC command prefix
     */
    public String getCommandPrefix() {
        return commandPrefix;
    }
}
