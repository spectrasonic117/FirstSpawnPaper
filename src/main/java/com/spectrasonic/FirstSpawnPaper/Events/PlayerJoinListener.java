package com.spectrasonic.FirstSpawnPaper.Events;

import com.spectrasonic.FirstSpawnPaper.Managers.FirstSpawnManager;
import com.spectrasonic.FirstSpawnPaper.Utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final FirstSpawnManager manager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!manager.isEnabled()) return;
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore() && manager.getFirstSpawnLocation() != null) {
            player.teleport(manager.getFirstSpawnLocation());
            player.setBedSpawnLocation(manager.getFirstSpawnLocation(), true);
            if (!manager.getWelcomeMessage().isEmpty()) {
                // Use MessageUtils to send the welcome message with MiniMessage formatting
                MessageUtils.sendMessage(player, manager.getWelcomeMessage());
            }
            manager.logDebug("Teleported new player " + player.getName() + " to first spawn location");
        }
    }
}