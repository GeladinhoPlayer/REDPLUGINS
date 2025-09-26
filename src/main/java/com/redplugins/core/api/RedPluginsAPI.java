package com.redplugins.core.api;

import com.redplugins.core.RedPluginsCore;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * API para outros plugins se conectarem ao RedPlugins Core
 */
public class RedPluginsAPI {
    
    /**
     * Conecta um plugin ao RedPlugins Core
     * @param plugin Plugin que quer se conectar
     * @return true se conectado com sucesso
     */
    public static boolean connectToCore(JavaPlugin plugin) {
        if (!RedPluginsCore.isAvailable()) {
            plugin.getLogger().severe("RedPlugins Core não está disponível!");
            return false;
        }
        
        return RedPluginsCore.getInstance().registerPlugin(plugin.getName());
    }
    
    /**
     * Desconecta um plugin do core
     * @param plugin Plugin que quer se desconectar
     */
    public static void disconnectFromCore(JavaPlugin plugin) {
        if (RedPluginsCore.isAvailable()) {
            RedPluginsCore.getInstance().unregisterPlugin(plugin.getName());
        }
    }
    
    /**
     * Verifica se o core está disponível
     * @return true se disponível
     */
    public static boolean isCoreAvailable() {
        return RedPluginsCore.isAvailable();
    }
    
    /**
     * Obtém instância do core
     * @return Instância do core ou null se não disponível
     */
    public static RedPluginsCore getCore() {
        return RedPluginsCore.getInstance();
    }
}
