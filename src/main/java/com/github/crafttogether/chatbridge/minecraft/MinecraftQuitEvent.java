package com.github.crafttogether.chatbridge.minecraft;

import com.github.crafttogether.chatbridge.irc.IrcMessageSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class MinecraftQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            IrcMessageSender.send(String.format("\u00034%s has left the server", event.getPlayer().getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
