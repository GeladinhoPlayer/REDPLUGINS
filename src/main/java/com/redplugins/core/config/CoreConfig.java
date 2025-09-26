package com.redplugins.core.config;

import com.redplugins.core.RedPluginsCore;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Level;

public class CoreConfig {
    
    private final RedPluginsCore plugin;
    private final FileConfiguration config;
    
    public CoreConfig(RedPluginsCore plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }
    
    // Configurações de logging
    public Level getLoggingLevel() {
        String level = config.getString("core.logging.level", "INFO");
        try {
            return Level.parse(level);
        } catch (IllegalArgumentException e) {
            return Level.INFO;
        }
    }
    
    public boolean isLoggingEnabled() {
        return config.getBoolean("core.logging.log-plugin-connections", true);
    }
    
    public boolean isPerformanceLoggingEnabled() {
        return config.getBoolean("core.logging.log-performance", true);
    }
    
    // Configurações de segurança
    public boolean isPluginValidationEnabled() {
        return config.getBoolean("core.security.validate-plugins", true);
    }
    
    public boolean isPermissionRequired() {
        return config.getBoolean("core.security.require-permission", false);
    }
    
    public List<String> getAllowedAuthors() {
        return config.getStringList("core.security.allowed-authors");
    }
    
    // Configurações de performance
    public boolean isMetricsEnabled() {
        return config.getBoolean("core.performance.enable-metrics", true);
    }
    
    public boolean isCacheEnabled() {
        return config.getBoolean("core.performance.cache-plugin-data", true);
    }
    
    public boolean isAsyncEnabled() {
        return config.getBoolean("core.performance.async-operations", true);
    }
    
    // Configurações de eventos
    public boolean areCustomEventsEnabled() {
        return config.getBoolean("core.events.enable-custom-events", true);
    }
    
    public boolean isBroadcastEnabled() {
        return config.getBoolean("core.events.broadcast-plugin-status", true);
    }
    
    // Mensagens personalizadas
    public String getMessage(String key) {
        String message = config.getString("core.messages." + key, "");
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    // Recarregar configuração
    public void reload() {
        plugin.reloadConfig();
    }
}
