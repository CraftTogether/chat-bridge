package xyz.crafttogether.chatbridge.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface Command {
    void invoke(SlashCommandEvent event);
    String getName();
    String getDescription();
}
