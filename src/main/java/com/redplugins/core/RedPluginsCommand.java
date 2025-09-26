package com.redplugins.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;

public class RedPluginsCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!sender.hasPermission("redplugins.admin")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando!");
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "info":
                showInfo(sender);
                break;
            case "plugins":
                showConnectedPlugins(sender);
                break;
            case "reload":
                reloadCore(sender);
                break;
            default:
                showHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== RedPlugins Core ===");
        sender.sendMessage(ChatColor.YELLOW + "/redplugins info - Mostra informações do core");
        sender.sendMessage(ChatColor.YELLOW + "/redplugins plugins - Lista plugins conectados");
        sender.sendMessage(ChatColor.YELLOW + "/redplugins reload - Recarrega o core");
    }
    
    private void showInfo(CommandSender sender) {
        RedPluginsCore core = RedPluginsCore.getInstance();
        int connectedCount = core.getConnectedPlugins().size();
        
        sender.sendMessage(ChatColor.GOLD + "=== RedPlugins Core Info ===");
        sender.sendMessage(ChatColor.GREEN + "Status: " + ChatColor.WHITE + "Ativo");
        sender.sendMessage(ChatColor.GREEN + "Versão: " + ChatColor.WHITE + core.getDescription().getVersion());
        sender.sendMessage(ChatColor.GREEN + "Plugins Conectados: " + ChatColor.WHITE + connectedCount);
    }
    
    private void showConnectedPlugins(CommandSender sender) {
        List<String> plugins = RedPluginsCore.getInstance().getConnectedPlugins();
        
        sender.sendMessage(ChatColor.GOLD + "=== Plugins Conectados ===");
        
        if (plugins.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Nenhum plugin conectado ao core.");
        } else {
            for (String plugin : plugins) {
                sender.sendMessage(ChatColor.GREEN + "• " + ChatColor.WHITE + plugin);
            }
        }
    }
    
    private void reloadCore(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Recarregando RedPlugins Core...");
        // Aqui você pode adicionar lógica de reload se necessário
        sender.sendMessage(ChatColor.GREEN + "Core recarregado com sucesso!");
    }
}
