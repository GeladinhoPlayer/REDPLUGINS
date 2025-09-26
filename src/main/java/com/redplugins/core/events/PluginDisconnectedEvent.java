package com.redplugins.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PluginDisconnectedEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private final String pluginName;
    
    public PluginDisconnectedEvent(String pluginName) {
        this.pluginName = pluginName;
    }
    
    public String getPluginName() {
        return pluginName;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
