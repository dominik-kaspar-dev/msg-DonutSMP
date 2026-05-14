package dev.domin.msgdonutsmp.commands;

import dev.domin.msgdonutsmp.MsgDonutSMP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnignoreCommand implements CommandExecutor {
    private final MsgDonutSMP plugin;

    public UnignoreCommand(MsgDonutSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("msg.use")) {
            plugin.send(p, "messages.no-permission");
            return true;
        }
        if (args.length != 1) {
            plugin.send(p, "messages.msgunignore-usage");
            return true;
        }
        String name = args[0];
        Player target = Bukkit.getPlayerExact(name);
        if (target == null) {
            plugin.send(p, "messages.player-not-found");
            return true;
        }

        plugin.getIgnoreManager().removeIgnore(p.getUniqueId(), target.getUniqueId());
        plugin.send(p, "messages.unignored");
        return true;
    }
}
