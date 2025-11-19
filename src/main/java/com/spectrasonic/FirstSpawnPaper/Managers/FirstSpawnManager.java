package com.spectrasonic.FirstSpawnPaper.Managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@RequiredArgsConstructor
public class FirstSpawnManager {

    private final JavaPlugin plugin;
    private Location firstSpawnLocation;
    private boolean enabled;
    private boolean debug;
    private boolean forceSpawn;
    private boolean welcomeMessageEnabled;
    private String welcomeMessagePrefix;
    private String welcomeMessage;
    private FileConfiguration config;

    public void loadConfig() {
        this.config = plugin.getConfig();
        this.enabled = config.getBoolean("enabled", true);
        this.debug = config.getBoolean("debug", false);
        this.forceSpawn = config.getBoolean("force_spawn", false);
        this.welcomeMessageEnabled = config.getBoolean("welcome-message.enabled", false);
        this.welcomeMessagePrefix = config.getString("welcome-message.prefix", "");
        this.welcomeMessage = config.getString("welcome-message.message", "");

        if (config.contains("firstSpawn")) {
            try {
                firstSpawnLocation = new Location(
                        plugin.getServer().getWorld(config.getString("firstSpawn.world")),
                        config.getDouble("firstSpawn.x"),
                        config.getDouble("firstSpawn.y"),
                        config.getDouble("firstSpawn.z"));

                String direction = config.getString("firstSpawn.direction", "");
                if (!direction.isEmpty()) {
                    switch (direction.toUpperCase()) {
                        case "NORTH":
                            firstSpawnLocation.setYaw(180f);
                            break;
                        case "EAST":
                            firstSpawnLocation.setYaw(270f);
                            break;
                        case "SOUTH":
                            firstSpawnLocation.setYaw(0f);
                            break;
                        case "WEST":
                            firstSpawnLocation.setYaw(90f);
                            break;
                        default:
                            logDebug("Invalid direction in config: " + direction);
                            break;
                    }
                }
                logDebug("Loaded spawn location: " + formatLocationRaw(firstSpawnLocation));
            } catch (Exception e) {
                plugin.getLogger().warning("Error loading spawn location: " + e.getMessage());
                firstSpawnLocation = null;
            }
        }
    }

    public void saveConfig() {
        if (firstSpawnLocation != null) {
            config.set("firstSpawn.world", firstSpawnLocation.getWorld().getName());
            config.set("firstSpawn.x", firstSpawnLocation.getX());
            config.set("firstSpawn.y", firstSpawnLocation.getY());
            config.set("firstSpawn.z", firstSpawnLocation.getZ());

            float yaw = firstSpawnLocation.getYaw();
            String direction = "";
            if (yaw >= 135 && yaw < 225) {
                direction = "NORTH";
            } else if (yaw >= 225 && yaw < 315) {
                direction = "EAST";
            } else if ((yaw >= 315 && yaw <= 360) || (yaw >= 0 && yaw < 45)) {
                direction = "SOUTH";
            } else if (yaw >= 45 && yaw < 135) {
                direction = "WEST";
            }
            config.set("firstSpawn.direction", direction);
        }
        config.set("enabled", enabled);
        config.set("debug", debug);
        config.set("force_spawn", forceSpawn);
        config.set("welcome-message.enabled", welcomeMessageEnabled);
        config.set("welcome-message.prefix", welcomeMessagePrefix);
        config.set("welcome-message.message", welcomeMessage);
        plugin.saveConfig();
    }

    public void logDebug(String message) {
        if (debug) {
            plugin.getLogger().info("[DEBUG] " + message);
        }
    }

    public String formatLocation(Location loc) {
        if (loc == null)
            return "Not set";
        return String.format("%s: %.1f, %.1f, %.1f", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
    }

    private String formatLocationRaw(Location loc) {
        if (loc == null)
            return "Not set";
        return String.format("%s: %.1f, %.1f, %.1f", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
    }

    public String getFormattedWelcomeMessage() {
        if (!welcomeMessageEnabled || welcomeMessage.isEmpty())
            return "";

        if (welcomeMessagePrefix != null && !welcomeMessagePrefix.isEmpty()) {
            return welcomeMessagePrefix + " " + welcomeMessage;
        }

        return welcomeMessage;
    }
}
