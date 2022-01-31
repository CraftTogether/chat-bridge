package xyz.crafttogether.chatbridge.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.crafttogether.chatbridge.discord.Command;
import xyz.crafttogether.weg.Weg;

public class OnlineCommand implements Command {
    private static final String name = "online";
    private static final String description = "View the online players";

    @Override
    public void invoke(SlashCommandEvent event) {
        StringBuilder players = new StringBuilder();
        StringBuilder afkPlayers = new StringBuilder();
        int afk = 0;
        int active = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Weg.isAfk(player.getUniqueId())) {
                afkPlayers.append("- ").append(player.getName()).append("\n");
                afk++;
                continue;
            }
            players.append("- ").append(player.getName()).append("\n");
            active++;
        }
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Players:")
                .addField("Online", players.toString(), false)
                .addField("AFK", afkPlayers.toString(), false)
                .setFooter(String.format("There are %d players connected, %d active and %d AFK", Bukkit.getOnlinePlayers().size(), active, afk))
                .build();
        event.replyEmbeds(embed).queue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
