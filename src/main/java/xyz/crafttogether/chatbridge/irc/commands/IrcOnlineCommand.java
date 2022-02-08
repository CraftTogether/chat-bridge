package xyz.crafttogether.chatbridge.irc.commands;

import dev.polarian.ircj.objects.events.PrivMessageEvent;
import xyz.crafttogether.chatbridge.irc.IrcCommand;

/**
 * Command which replies who are online on the IRC server
 */
public class IrcOnlineCommand implements IrcCommand {
    @Override
    public void invoke(PrivMessageEvent event) {
    }
}
