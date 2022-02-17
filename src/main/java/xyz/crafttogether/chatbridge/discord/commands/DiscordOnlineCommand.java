package xyz.crafttogether.chatbridge.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.crafttogether.craftcore.discord.DiscordCommand;
import xyz.crafttogether.weg.Weg;

/**
 * Command to get the current online players on the minecraft server
 */
public class DiscordOnlineCommand implements DiscordCommand {
    private static final String NAME = "online";
    private static final String DESCRIPTION = "Check who is online on the server";

    @Override
    public void invoke(SlashCommandInteractionEvent event) {
        StringBuilder players = new StringBuilder();
        StringBuilder afkPlayers = new StringBuilder();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Weg.isAfk(player.getUniqueId())) {
                afkPlayers.append("- ").append(player.getName()).append("\n");
                continue;
            }
            players.append("- ").append(player.getName()).append("\n");
        }
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Players:")
                .addField("Online", players.toString(), false)
                .addField("AFK", afkPlayers.toString(), false)
                .setFooter(String.format("There are %d players connected, %d active and %d AFK", Bukkit.getOnlinePlayers().size(), Bukkit.getOnlinePlayers().size(), Weg.getAfkPlayers()))
                .build();
        event.replyEmbeds(embed).queue();
    }

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public String getCommandDescription() {
        return DESCRIPTION;
    }
}
