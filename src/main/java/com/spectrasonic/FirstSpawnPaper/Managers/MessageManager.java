package com.spectrasonic.FirstSpawnPaper.Managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class MessageManager {

    private final JavaPlugin plugin;
    private final Map<String, String> messages = new HashMap<>();

    public void loadMessages() {
        FileConfiguration config = plugin.getConfig();
        messages.clear();

        loadSection(config, "messages", "");
    }

    private void loadSection(FileConfiguration config, String section, String prefix) {
        if (!config.contains(section)) return;

        for (String key : config.getConfigurationSection(section).getKeys(false)) {
            String fullPath = section + "." + key;

            if (config.isConfigurationSection(fullPath)) {
                loadSection(config, fullPath, prefix + key + ".");
            } else {
                String value = config.getString(fullPath, "");
                messages.put(prefix + key, value);
            }
        }
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "Unknown message: " + key);
    }

    public String getMessage(String key, Map<String, String> placeholders) {
        String message = getMessage(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }
}
