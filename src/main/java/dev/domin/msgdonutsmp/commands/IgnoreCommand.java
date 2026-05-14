package dev.domin.msgdonutsmp.commands;

import dev.domin.msgdonutsmp.MsgDonutSMP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IgnoreCommand implements CommandExecutor {

    private final MsgDonutSMP plugin;

    public IgnoreCommand(MsgDonutSMP plugin) {
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
            plugin.send(p, "messages.msgignore-usage");
            return true;
        }
        String name = args[0];
        Player target = Bukkit.getPlayerExact(name);
        if (target == null) {
            plugin.send(p, "messages.player-not-found");
            return true;
        }

        // disallow ignoring staff if config says so
        boolean ignoreStaff = plugin.getConfig().getBoolean("options.allow-ignore-staff", true);
        if (!ignoreStaff && target.hasPermission("msg.bypass.ignore")) {
            plugin.send(p, "messages.cannot-ignore-staff");
            return true;
        }

        plugin.getIgnoreManager().addIgnore(p.getUniqueId(), target.getUniqueId());
        plugin.send(p, "messages.ignored");
        return true;
    }
}
