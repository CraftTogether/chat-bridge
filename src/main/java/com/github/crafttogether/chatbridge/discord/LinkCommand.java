package com.github.crafttogether.chatbridge.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LinkCommand extends ListenerAdapter {

    public static final HashMap<String, String> verify = new HashMap<>();

    private final Character[] chars = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private String getRandomCode() {
        final StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            final int random = new Random().nextInt(chars.length); // Get random number
            code.append(chars[random]);
        }
        return code.toString();
    }

    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("link")) {
            Bukkit.getConsoleSender().sendMessage("ok!");
            event.getUser().openPrivateChannel().queue(channel -> {
                String randomCode = getRandomCode(); // Get random code
                // Generate new codes as long as the code is already in use
                while (verify.containsKey(randomCode)) {
                    randomCode = getRandomCode();
                }
                String finalRandomCode = randomCode;
                verify.put(finalRandomCode, event.getUser().getId()); // Add code to HashMap
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                executor.schedule(() -> verify.remove(finalRandomCode), 2, TimeUnit.MINUTES);

                final EmbedBuilder verifyCode = new EmbedBuilder()
                        .setTitle("Verify")
                        .setDescription("Verify your account and type in the minecraft chat the following command\n```/verify " + randomCode + "```");
                channel.sendMessageEmbeds(verifyCode.build()).queue(); // Send dm
            });
            event.reply(":thumbsup: Check your dms!").setEphemeral(true).queue();
        }

    }

}
