package xyz.crafttogether.chatbridge.configuration.sections;

import xyz.crafttogether.chatbridge.configuration.ConfigSection;

public class IrcConfigSection extends ConfigSection {
    private boolean enabled;
    private String username;
    private String hostname;
    private int port;
    private boolean tls;
    private int timeout;
    private String channel;
    private String prefix;
    private int reconnectAttempts;
    private int reconnectDelay;
    private String commandPrefix;

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

    public boolean isEnabled() {
        return enabled;
    }

    public String getUsername() {
        return username;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public boolean isTlsEnabled() {
        return tls;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getChannel() {
        return channel;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getReconnectAttempts() {
        return reconnectAttempts;
    }

    public int getReconnectDelay() {
        return reconnectDelay;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }
}
