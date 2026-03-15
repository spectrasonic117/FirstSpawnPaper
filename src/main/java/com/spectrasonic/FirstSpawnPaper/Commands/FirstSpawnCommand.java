package com.spectrasonic.FirstSpawnPaper.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.spectrasonic.FirstSpawnPaper.Managers.FirstSpawnManager;
import com.spectrasonic.FirstSpawnPaper.Utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

@CommandAlias("firstspawn|fs")
@RequiredArgsConstructor
public class FirstSpawnCommand extends BaseCommand {

    private final FirstSpawnManager manager;

    @Default
    @Description("Mostrar ayuda de FirstSpawn")
    public void onDefault(CommandSender sender) {
        MessageUtils.sendConfigMessage(sender, "help.header");
        MessageUtils.sendConfigMessage(sender, "help.set");
        MessageUtils.sendConfigMessage(sender, "help.status");
        MessageUtils.sendConfigMessage(sender, "help.test");
        MessageUtils.sendConfigMessage(sender, "help.toggle");
        MessageUtils.sendConfigMessage(sender, "help.forcespawn");
        MessageUtils.sendConfigMessage(sender, "help.welcomemessage");
        MessageUtils.sendConfigMessage(sender, "help.reload");
        MessageUtils.sendConfigMessage(sender, "help.debug");
    }

    @Subcommand("set")
    @Description("Establecer la ubicación de spawn")
    @CommandPermission("firstspawn.set")
    public void onSet(Player player) {
        Location loc = player.getLocation().clone();
        loc.setPitch(0f);
        manager.setFirstSpawnLocation(loc);
        manager.saveConfig();
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("location", manager.formatLocation(loc));
        MessageUtils.sendConfigMessage(player, "spawn.set", placeholders);
        manager.logDebug("Set spawn location to: " + manager.formatLocation(loc));
    }

    @Subcommand("status")
    @Description("Mostrar el estado de FirstSpawn")
    @CommandPermission("firstspawn.status")
    public void onStatus(CommandSender sender) {
        MessageUtils.sendConfigMessage(sender, "status.header");

        Map<String, String> placeholders;
        placeholders = new HashMap<>();
        placeholders.put("status", manager.isEnabled() ? "<green>Sí</green>" : "<red>No</red>");
        MessageUtils.sendConfigMessage(sender, "status.enabled", placeholders);

        placeholders = new HashMap<>();
        placeholders.put("status", manager.isDebug() ? "<green>Habilitado</green>" : "<red>Deshabilitado</red>");
        MessageUtils.sendConfigMessage(sender, "status.debug", placeholders);

        placeholders = new HashMap<>();
        placeholders.put("status", manager.isForceSpawn() ? "<green>Habilitado</green>" : "<red>Deshabilitado</red>");
        MessageUtils.sendConfigMessage(sender, "status.force_spawn", placeholders);

        placeholders = new HashMap<>();
        placeholders.put("location", manager.formatLocation(manager.getFirstSpawnLocation()));
        MessageUtils.sendConfigMessage(sender, "status.location", placeholders);

        placeholders = new HashMap<>();
        placeholders.put("status", manager.isWelcomeMessageEnabled() ? "<green>Habilitado</green>" : "<red>Deshabilitado</red>");
        MessageUtils.sendConfigMessage(sender, "status.welcome", placeholders);

        if (manager.isWelcomeMessageEnabled()) {
            String formattedMessage = manager.getFormattedWelcomeMessage();
            if (!formattedMessage.isEmpty()) {
                placeholders = new HashMap<>();
                placeholders.put("message", formattedMessage);
                MessageUtils.sendConfigMessage(sender, "status.welcome_message", placeholders);
            }
        }
    }

    @Subcommand("test")
    @Description("Teleportarse de prueba al spawn")
    @CommandPermission("firstspawn.test")
    public void onTest(Player player) {
        if (manager.getFirstSpawnLocation() == null) {
            MessageUtils.sendConfigMessage(player, "spawn.not_set");
            return;
        }
        player.teleport(manager.getFirstSpawnLocation());
        MessageUtils.sendConfigMessage(player, "spawn.teleported");
        String formattedWelcomeMessage = manager.getFormattedWelcomeMessage();
        if (!formattedWelcomeMessage.isEmpty()) {
            MessageUtils.sendRawMessage(player, formattedWelcomeMessage);
        }
    }

    @Subcommand("toggle")
    @Description("Alternar la habilitación del plugin")
    @CommandPermission("firstspawn.toggle")
    public void onToggle(CommandSender sender) {
        manager.setEnabled(!manager.isEnabled());
        manager.saveConfig();
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("status", manager.isEnabled() ? "<green>habilitado</green>" : "<red>deshabilitado</red>");
        MessageUtils.sendConfigMessage(sender, "toggle.success", placeholders);
    }

    @Subcommand("debug")
    @Description("Alternar el modo debug")
    @CommandPermission("firstspawn.debug")
    public void onDebug(CommandSender sender) {
        manager.setDebug(!manager.isDebug());
        manager.saveConfig();
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("status", manager.isDebug() ? "<green>habilitado</green>" : "<red>deshabilitado</red>");
        MessageUtils.sendConfigMessage(sender, "toggle.debug", placeholders);
    }

    @Subcommand("forcespawn")
    @Description("Configurar si se fuerza el spawn en cada entrada")
    @CommandPermission("firstspawn.forcespawn")
    @CommandCompletion("true|false")
    public void onForceSpawn(CommandSender sender, boolean value) {
        manager.setForceSpawn(value);
        manager.saveConfig();
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("status", value ? "<green>habilitado</green>" : "<red>deshabilitado</red>");
        MessageUtils.sendConfigMessage(sender, "forcespawn.success", placeholders);
        if (value) {
            MessageUtils.sendConfigMessage(sender, "forcespawn.enabled");
        } else {
            MessageUtils.sendConfigMessage(sender, "forcespawn.disabled");
        }
    }

    @Subcommand("welcomemessage")
    @Description("Configurar si se muestra el mensaje de bienvenida")
    @CommandPermission("firstspawn.welcomemessage")
    @CommandCompletion("true|false")
    public void onWelcomeMessage(CommandSender sender, boolean value) {
        manager.setWelcomeMessageEnabled(value);
        manager.saveConfig();
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("status", value ? "<green>habilitado</green>" : "<red>deshabilitado</red>");
        MessageUtils.sendConfigMessage(sender, "welcomemessage.success", placeholders);
        if (value) {
            MessageUtils.sendConfigMessage(sender, "welcomemessage.enabled");
        } else {
            MessageUtils.sendConfigMessage(sender, "welcomemessage.disabled");
        }
    }

    @Subcommand("reload")
    @Description("Recargar la configuración")
    @CommandPermission("firstspawn.reload")
    public void onReload(CommandSender sender) {
        manager.loadConfig();
        MessageUtils.setPrefix(manager.getChatPrefix());
        MessageUtils.setManager(manager.getMessageManager());
        MessageUtils.sendConfigMessage(sender, "reload.success");
    }
}