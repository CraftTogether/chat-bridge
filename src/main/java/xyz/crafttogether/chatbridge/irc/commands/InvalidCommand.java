package xyz.crafttogether.chatbridge.irc.commands;

import dev.polarian.ircj.objects.messages.PrivMessage;
import xyz.crafttogether.chatbridge.irc.IrcCommand;

import java.io.IOException;

public class InvalidCommand implements IrcCommand {
    @Override
    public void invoke(PrivMessage message) {
        try {
            message.sendMessage(message.getChannel(), "Invalid command");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
