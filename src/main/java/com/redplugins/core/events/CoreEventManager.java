package com.redplugins.core.events;

import com.redplugins.core.RedPluginsCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class CoreEventManager implements Listener {
    
    private final RedPluginsCore core;
    
    public CoreEventManager(RedPluginsCore core) {
        this.core = core;
    }
    
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin plugin = event.getPlugin();
        
        // Verifica se o plugin depende do core
        if (plugin.getDescription().getDepend().contains("REDPLUGINS")) {
            // Registra automaticamente plugins que dependem do core
            Bukkit.getScheduler().runTaskLater(core, () -> {
                core.registerPlugin(plugin.getName());
            }, 1L);
        }
    }
    
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        
        // Remove plugin do core se estava conectado
        if (core.isPluginConnected(plugin.getName())) {
            core.unregisterPlugin(plugin.getName());
        }
    }
    
    /**
     * Dispara evento personalizado quando plugin se conecta
     */
    public void firePluginConnectedEvent(Plugin plugin) {
        if (core.getCoreConfig().areCustomEventsEnabled()) {
            PluginConnectedEvent event = new PluginConnectedEvent(plugin);
            Bukkit.getPluginManager().callEvent(event);
        }
    }
    
    /**
     * Dispara evento personalizado quando plugin se desconecta
     */
    public void firePluginDisconnectedEvent(String pluginName) {
        if (core.getCoreConfig().areCustomEventsEnabled()) {
            PluginDisconnectedEvent event = new PluginDisconnectedEvent(pluginName);
            Bukkit.getPluginManager().callEvent(event);
        }
    }
    
    public void shutdown() {
        // Cleanup se necessário
    }
}
