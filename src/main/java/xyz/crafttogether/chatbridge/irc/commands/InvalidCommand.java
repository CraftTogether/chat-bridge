package xyz.crafttogether.chatbridge.irc.commands;

import dev.polarian.ircj.objects.events.PrivMessageEvent;
import xyz.crafttogether.chatbridge.irc.IrcCommand;

import java.io.IOException;

/**
 * Command which is executed when the slash command is invalid
 */
public class InvalidCommand implements IrcCommand {
    @Override
    public void invoke(PrivMessageEvent event) {
        try {
            event.sendMessage(event.getChannel(), "Invalid command");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
