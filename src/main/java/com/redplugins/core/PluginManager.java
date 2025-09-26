package com.redplugins.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginManager implements Listener {
    
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        String pluginName = event.getPlugin().getName();
        
        // Verifica se o plugin depende do RedPlugins
        if (event.getPlugin().getDescription().getDepend().contains("REDPLUGINS")) {
            RedPluginsCore.getInstance().registerPlugin(pluginName);
        }
    }
    
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        String pluginName = event.getPlugin().getName();
        
        // Remove o plugin da lista se estava conectado
        if (RedPluginsCore.getInstance().isPluginConnected(pluginName)) {
            RedPluginsCore.getInstance().unregisterPlugin(pluginName);
        }
    }
}
