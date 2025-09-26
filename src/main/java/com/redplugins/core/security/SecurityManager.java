package com.redplugins.core.security;

import com.redplugins.core.config.CoreConfig;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

public class SecurityManager {
    
    private final CoreConfig config;
    
    public SecurityManager(CoreConfig config) {
        this.config = config;
    }
    
    /**
     * Valida se um plugin pode se conectar ao core
     */
    public boolean validatePlugin(Plugin plugin) {
        if (!config.isPluginValidationEnabled()) {
            return true;
        }
        
        PluginDescriptionFile desc = plugin.getDescription();
        
        // Verifica se o plugin tem dependência correta
        if (!desc.getDepend().contains("REDPLUGINS")) {
            return false;
        }
        
        // Verifica autores permitidos (se configurado)
        List<String> allowedAuthors = config.getAllowedAuthors();
        if (!allowedAuthors.isEmpty()) {
            List<String> authors = desc.getAuthors();
            if (authors.isEmpty() || !allowedAuthors.containsAll(authors)) {
                return false;
            }
        }
        
        // Verifica se o plugin está habilitado
        if (!plugin.isEnabled()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Verifica se um plugin tem permissão para executar operações específicas
     */
    public boolean hasPermission(Plugin plugin, String permission) {
        if (!config.isPermissionRequired()) {
            return true;
        }
        
        // Implementar lógica de permissões específicas se necessário
        return true;
    }
}
