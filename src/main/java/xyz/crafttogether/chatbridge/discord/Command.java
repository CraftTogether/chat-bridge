package xyz.crafttogether.chatbridge.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Interface for discord commands
 */
public interface Command {
    void invoke(SlashCommandInteractionEvent event);
    String getName();
    String getDescription();
}
