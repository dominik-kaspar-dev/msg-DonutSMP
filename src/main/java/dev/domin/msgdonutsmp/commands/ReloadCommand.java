package dev.domin.msgdonutsmp.commands;

import dev.domin.msgdonutsmp.MsgDonutSMP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final MsgDonutSMP plugin;

    public ReloadCommand(MsgDonutSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("msg.admin")) {
            sender.sendMessage(plugin.getConfig().getString("messages.no-permission","&cNo permission."));
            return true;
        }
        plugin.reloadConfig();
        plugin.getIgnoreManager().load();
        sender.sendMessage(plugin.getConfig().getString("messages.reloaded","&aReloaded."));
        return true;
    }
}
