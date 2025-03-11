package com.spectrasonic.FirstSpawnPaper;

import co.aikar.commands.PaperCommandManager;
import com.spectrasonic.FirstSpawnPaper.Utils.MessageUtils;
import com.spectrasonic.FirstSpawnPaper.Commands.FirstSpawnCommand;
import com.spectrasonic.FirstSpawnPaper.Events.PlayerJoinListener;
import com.spectrasonic.FirstSpawnPaper.Managers.FirstSpawnManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private FirstSpawnManager manager;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        
        saveDefaultConfig();
        manager = new FirstSpawnManager(this);
        manager.loadConfig();

        if (manager.isDebug()) {
            getLogger().info("[DEBUG] FirstSpawn has been enabled!");
        } else {
            getLogger().info("FirstSpawn has been enabled!");
        }

        registerCommands();
        registerEvents();
        MessageUtils.sendStartupMessage(this);

    }

    @Override
    public void onDisable() {
        MessageUtils.sendShutdownMessage(this);
        
    }

    public void registerCommands() {
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new FirstSpawnCommand(manager));
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(manager), this);
    }
}
