package com.redplugins.core;

import com.redplugins.core.config.CoreConfig;
import com.redplugins.core.events.CoreEventManager;
import com.redplugins.core.metrics.CoreMetrics;
import com.redplugins.core.security.SecurityManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class RedPluginsCore extends JavaPlugin {
    
    private static RedPluginsCore instance;
    private final Map<String, PluginInfo> connectedPlugins = new ConcurrentHashMap<>();
    private final List<String> pluginLoadOrder = new CopyOnWriteArrayList<>();
    
    private CoreConfig coreConfig;
    private PluginManager pluginManager;
    private SecurityManager securityManager;
    private CoreEventManager eventManager;
    private CoreMetrics metrics;
    
    @Override
    public void onEnable() {
        instance = this;
        
        initializeCore();
        
        getLogger().info("RedPlugins Core iniciando...");
        
        // Aguarda um tick para verificar plugins dependentes
        Bukkit.getScheduler().runTaskLater(this, this::checkConnectedPlugins, 1L);
    }
    
    @Override
    public void onDisable() {
        if (eventManager != null) {
            eventManager.shutdown();
        }
        if (metrics != null) {
            metrics.shutdown();
        }
        
        String shutdownMessage = coreConfig != null ? 
            coreConfig.getMessage("core-shutdown") : 
            "§c[REDPLUGINS] Core desabilitado!";
        Bukkit.getConsoleSender().sendMessage(shutdownMessage);
        
        connectedPlugins.clear();
        pluginLoadOrder.clear();
        instance = null;
    }
    
    private void initializeCore() {
        try {
            // Salva configuração padrão
            saveDefaultConfig();
            
            // Inicializa componentes
            coreConfig = new CoreConfig(this);
            securityManager = new SecurityManager(coreConfig);
            pluginManager = new PluginManager(this);
            eventManager = new CoreEventManager(this);
            metrics = new CoreMetrics(this);
            
            // Registra eventos
            getServer().getPluginManager().registerEvents(pluginManager, this);
            getServer().getPluginManager().registerEvents(eventManager, this);
            
            // Registra comandos
            getCommand("redplugins").setExecutor(new RedPluginsCommand(this));
            
            getLogger().info("Componentes do core inicializados com sucesso!");
            
        } catch (Exception e) {
            getLogger().severe("Erro ao inicializar o core: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    private void checkConnectedPlugins() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin != this && plugin.getDescription().getDepend().contains("REDPLUGINS")) {
                if (plugin.isEnabled()) {
                    registerPluginInternal(plugin.getName(), plugin);
                }
            }
        }
        
        String message = coreConfig.getMessage("core-started")
            .replace("{count}", String.valueOf(connectedPlugins.size()));
        
        Bukkit.getConsoleSender().sendMessage(message);
        
        if (coreConfig.isLoggingEnabled() && !connectedPlugins.isEmpty()) {
            getLogger().info("Plugins conectados: " + String.join(", ", connectedPlugins.keySet()));
        }
        
        if (metrics != null) {
            metrics.startMetrics();
        }
    }
    
    private boolean registerPluginInternal(String pluginName, Plugin plugin) {
        if (connectedPlugins.containsKey(pluginName)) {
            return false;
        }
        
        // Validação de segurança
        if (!securityManager.validatePlugin(plugin)) {
            getLogger().warning("Plugin " + pluginName + " falhou na validação de segurança!");
            return false;
        }
        
        PluginInfo info = new PluginInfo(pluginName, plugin, System.currentTimeMillis());
        connectedPlugins.put(pluginName, info);
        pluginLoadOrder.add(pluginName);
        
        if (coreConfig.isLoggingEnabled()) {
            String message = coreConfig.getMessage("plugin-connected")
                .replace("{plugin}", pluginName);
            Bukkit.getConsoleSender().sendMessage(message);
        }
        
        // Dispara evento personalizado
        eventManager.firePluginConnectedEvent(plugin);
        
        return true;
    }
    
    public boolean registerPlugin(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin == null) {
            return false;
        }
        
        return registerPluginInternal(pluginName, plugin);
    }
    
    public void unregisterPlugin(String pluginName) {
        PluginInfo info = connectedPlugins.remove(pluginName);
        if (info != null) {
            pluginLoadOrder.remove(pluginName);
            
            if (coreConfig.isLoggingEnabled()) {
                String message = coreConfig.getMessage("plugin-disconnected")
                    .replace("{plugin}", pluginName);
                Bukkit.getConsoleSender().sendMessage(message);
            }
            
            // Dispara evento de desconexão
            eventManager.firePluginDisconnectedEvent(pluginName);
        }
    }
    
    public CoreConfig getCoreConfig() { return coreConfig; }
    public SecurityManager getSecurityManager() { return securityManager; }
    public CoreEventManager getEventManager() { return eventManager; }
    public CoreMetrics getMetrics() { return metrics; }
    
    public boolean isPluginConnected(String pluginName) {
        return connectedPlugins.containsKey(pluginName);
    }
    
    public Map<String, PluginInfo> getConnectedPluginsInfo() {
        return new ConcurrentHashMap<>(connectedPlugins);
    }
    
    public List<String> getConnectedPlugins() {
        return new CopyOnWriteArrayList<>(connectedPlugins.keySet());
    }
    
    public List<String> getPluginLoadOrder() {
        return new CopyOnWriteArrayList<>(pluginLoadOrder);
    }
    
    public static RedPluginsCore getInstance() {
        return instance;
    }
    
    public static boolean isAvailable() {
        return instance != null && instance.isEnabled();
    }
    
    public static class PluginInfo {
        private final String name;
        private final Plugin plugin;
        private final long connectTime;
        private long lastActivity;
        
        public PluginInfo(String name, Plugin plugin, long connectTime) {
            this.name = name;
            this.plugin = plugin;
            this.connectTime = connectTime;
            this.lastActivity = connectTime;
        }
        
        // Getters
        public String getName() { return name; }
        public Plugin getPlugin() { return plugin; }
        public long getConnectTime() { return connectTime; }
        public long getLastActivity() { return lastActivity; }
        public void updateActivity() { this.lastActivity = System.currentTimeMillis(); }
    }
}
