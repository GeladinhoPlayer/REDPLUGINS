package com.redplugins.core.metrics;

import com.redplugins.core.RedPluginsCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class CoreMetrics {
    
    private final RedPluginsCore core;
    private final Map<String, Long> performanceData = new ConcurrentHashMap<>();
    private BukkitTask metricsTask;
    
    public CoreMetrics(RedPluginsCore core) {
        this.core = core;
    }
    
    public void startMetrics() {
        if (!core.getCoreConfig().isMetricsEnabled()) {
            return;
        }
        
        // Coleta métricas a cada 5 minutos
        metricsTask = Bukkit.getScheduler().runTaskTimerAsynchronously(core, () -> {
            collectMetrics();
        }, 6000L, 6000L); // 5 minutos
    }
    
    private void collectMetrics() {
        try {
            // Coleta dados de performance
            long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            long totalMemory = Runtime.getRuntime().totalMemory();
            
            performanceData.put("memory_used", usedMemory);
            performanceData.put("memory_total", totalMemory);
            performanceData.put("plugins_connected", (long) core.getConnectedPlugins().size());
            performanceData.put("uptime", System.currentTimeMillis());
            
            if (core.getCoreConfig().isPerformanceLoggingEnabled()) {
                core.getLogger().info(String.format(
                    "Métricas - Plugins: %d, Memória: %.2f MB / %.2f MB",
                    core.getConnectedPlugins().size(),
                    usedMemory / 1024.0 / 1024.0,
                    totalMemory / 1024.0 / 1024.0
                ));
            }
            
        } catch (Exception e) {
            core.getLogger().warning("Erro ao coletar métricas: " + e.getMessage());
        }
    }
    
    public Map<String, Long> getPerformanceData() {
        return new ConcurrentHashMap<>(performanceData);
    }
    
    public void shutdown() {
        if (metricsTask != null) {
            metricsTask.cancel();
        }
    }
}
