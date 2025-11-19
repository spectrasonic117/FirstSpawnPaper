package com.spectrasonic.FirstSpawnPaper.Events;

import com.spectrasonic.FirstSpawnPaper.Managers.FirstSpawnManager;
import com.spectrasonic.FirstSpawnPaper.Utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class PlayerJoinListener implements Listener {

    private final FirstSpawnManager manager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!manager.isEnabled())
            return;

        Player player = event.getPlayer();

        if (manager.getFirstSpawnLocation() == null)
            return;

        // Si force_spawn está habilitado, teleportar siempre
        // Si está deshabilitado, solo teleportar si es la primera vez que entra
        boolean shouldTeleport = manager.isForceSpawn() || !player.hasPlayedBefore();

        if (shouldTeleport) {
            player.teleport(manager.getFirstSpawnLocation());
            player.setBedSpawnLocation(manager.getFirstSpawnLocation(), true);

            if (!manager.getWelcomeMessage().isEmpty()) {
                MessageUtils.sendMessage(player, manager.getWelcomeMessage());
            }

            manager.logDebug("Teleported player " + player.getName() + " to first spawn location (force_spawn: "
                    + manager.isForceSpawn() + ", new player: " + !player.hasPlayedBefore() + ")");
        }
    }
}