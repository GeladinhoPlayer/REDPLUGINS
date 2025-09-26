package com.redplugins.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class PluginConnectedEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private final Plugin plugin;
    
    public PluginConnectedEvent(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
