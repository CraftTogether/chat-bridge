package xyz.crafttogether.chatbridge.irc;

import dev.polarian.ircj.objects.messages.PrivMessage;

import java.util.HashMap;

public class CommandHandler {
    private static final HashMap<String, IrcCommand> commands = new HashMap<>();
    private static IrcCommand invalidCommandHandler = null;

    public static void parseCommand(PrivMessage message, String prefix) {
        int separator = message.getMessage().indexOf(" ");
        IrcCommand command;
        if (separator == -1) {
            command = commands.getOrDefault(message.getMessage().substring(prefix.length()), invalidCommandHandler);
        } else {
            command = commands.getOrDefault(message.getMessage().substring(prefix.length(), separator), invalidCommandHandler);
        }
        if (command == null) {
            return;
        }
        command.invoke(message);
    }

    public static void setInvalidCommandHandler(IrcCommand commandHandler) {
        invalidCommandHandler = commandHandler;
    }

    public static void clearCommands() {
        commands.clear();
        invalidCommandHandler = null;
    }

    public static void addCommand(String commandName, IrcCommand command) {
        commands.put(commandName, command);
    }
}
