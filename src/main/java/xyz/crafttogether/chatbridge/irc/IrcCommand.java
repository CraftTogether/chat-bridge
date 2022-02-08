package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.objects.events.PrivMessageEvent;

/**
 * Interface used by IrcCommands which can be invoked when the command is received
 */
public interface IrcCommand {
    void invoke(PrivMessageEvent event);
}
