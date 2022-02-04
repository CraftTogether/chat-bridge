package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.objects.messages.PrivMessage;

public interface IrcCommand {
    void invoke(PrivMessage message);
}
