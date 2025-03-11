package com.spectrasonic.FirstSpawnPaper.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.spectrasonic.FirstSpawnPaper.Managers.FirstSpawnManager;
import com.spectrasonic.FirstSpawnPaper.Utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("firstspawn|fs")
@RequiredArgsConstructor
public class FirstSpawnCommand extends BaseCommand {

    private final FirstSpawnManager manager;

    @Default
    @Description("Mostrar ayuda de FirstSpawn")
    public void onDefault(CommandSender sender) {
        MessageUtils.sendMessage(sender, "<yellow>FirstSpawn Commands:</yellow>");
        MessageUtils.sendMessage(sender, "<gray>/firstspawn set</gray> - <white>Setear la ubicación de spawn</white>");
        MessageUtils.sendMessage(sender, "<gray>/firstspawn status</gray> - <white>Mostrar la configuración actual</white>");
        MessageUtils.sendMessage(sender, "<gray>/firstspawn test</gray> - <white>Teleportarse para prueba al spawn</white>");
        MessageUtils.sendMessage(sender, "<gray>/firstspawn toggle</gray> - <white>Habilitar/Deshabilitar el plugin</white>");
        MessageUtils.sendMessage(sender, "<gray>/firstspawn reload</gray> - <white>Recargar la configuración</white>");
        MessageUtils.sendMessage(sender, "<gray>/firstspawn debug</gray> - <white>Alternar el modo debug</white>");
    }

    @Subcommand("set")
    @Description("Establecer la ubicación de spawn")
    @CommandPermission("firstspawn.set")
    public void onSet(Player player) {
        Location loc = player.getLocation();
        manager.setFirstSpawnLocation(loc);
        manager.saveConfig();
        MessageUtils.sendMessage(player, "<green>La ubicación de primer spawn ha sido establecida a tu posición actual:</green> <yellow>" + manager.formatLocation(loc) + "</yellow>");
        manager.logDebug("Set spawn location to: " + manager.formatLocation(loc));
    }

    @Subcommand("status")
    @Description("Mostrar el estado de FirstSpawn")
    @CommandPermission("firstspawn.status")
    public void onStatus(CommandSender sender) {
        MessageUtils.sendMessage(sender, "<yellow>Estado de FirstSpawn:</yellow>");
        MessageUtils.sendMessage(sender, "<gray>Plugin habilitado:</gray> " + (manager.isEnabled() ? "<green>Sí</green>" : "<red>No</red>"));
        MessageUtils.sendMessage(sender, "<gray>Modo debug:</gray> " + (manager.isDebug() ? "<green>Habilitado</green>" : "<red>Deshabilitado</red>"));
        MessageUtils.sendMessage(sender, "<gray>Ubicación actual del spawn:</gray> <yellow>" + manager.formatLocation(manager.getFirstSpawnLocation()) + "</yellow>");
        MessageUtils.sendMessage(sender, "<gray>Mensaje de bienvenida:</gray> " + (manager.getWelcomeMessage().isEmpty() ? "<red>Ninguno</red>" : "<green>" + manager.getWelcomeMessage() + "</green>"));
    }

    @Subcommand("test")
    @Description("Teleportarse de prueba al spawn")
    @CommandPermission("firstspawn.test")
    public void onTest(Player player) {
        if (manager.getFirstSpawnLocation() == null) {
            MessageUtils.sendMessage(player, "<red>La ubicación de spawn no está establecida!</red>");
            return;
        }
        player.teleport(manager.getFirstSpawnLocation());
        MessageUtils.sendMessage(player, "<green>Teletransportado a la ubicación de primer spawn!</green>");
        if (!manager.getWelcomeMessage().isEmpty()) {
            MessageUtils.sendMessage(player, manager.getWelcomeMessage());
        }
    }

    @Subcommand("toggle")
    @Description("Alternar la habilitación del plugin")
    @CommandPermission("firstspawn.toggle")
    public void onToggle(CommandSender sender) {
        manager.setEnabled(!manager.isEnabled());
        manager.saveConfig();
        MessageUtils.sendMessage(sender, "FirstSpawn ahora está " + (manager.isEnabled() ? "<green>habilitado</green>" : "<red>deshabilitado</red>"));
    }

    @Subcommand("debug")
    @Description("Alternar el modo debug")
    public void onDebug(CommandSender sender) {
        manager.setDebug(!manager.isDebug());
        manager.saveConfig();
        MessageUtils.sendMessage(sender, "Modo debug ahora está " + (manager.isDebug() ? "<green>habilitado</green>" : "<red>deshabilitado</red>"));
    }

    @Subcommand("reload")
    @Description("Recargar la configuración")
    @CommandPermission("firstspawn.reload")
    public void onReload(CommandSender sender) {
        manager.loadConfig();
        MessageUtils.sendMessage(sender, "<green>La configuración ha sido recargada!</green>");
    }
}