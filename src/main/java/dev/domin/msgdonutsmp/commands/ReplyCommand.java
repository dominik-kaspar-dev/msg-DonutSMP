package dev.domin.msgdonutsmp.commands;

import dev.domin.msgdonutsmp.MsgDonutSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand implements CommandExecutor {

    private final MsgDonutSMP plugin;

    public ReplyCommand(MsgDonutSMP plugin) {
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
        if (args.length < 1) {
            p.sendMessage(plugin.getConfig().getString("messages.r-usage","&cUsage: /r <message>"));
            return true;
        }
        UUID other = MsgCommand.last.get(p.getUniqueId());
        if (other == null) {
            plugin.send(p, "messages.no-reply");
            return true;
        }
        Player target = Bukkit.getPlayer(other);
        if (target == null) {
            plugin.send(p, "messages.player-not-found");
            return true;
        }

        if (plugin.isVanished(p, target) && !p.hasPermission("msg.bypass.vanish")) {
            plugin.send(p, "messages.player-not-found");
            return true;
        }

        // ignore check
        if (plugin.getIgnoreManager().isIgnored(target.getUniqueId(), p.getUniqueId())) {
            plugin.send(p, "messages.ignoring-you");
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(s);
        }
        String message = sb.toString();
        MiniMessage mm = MiniMessage.miniMessage();
        String sendFormat = plugin.getConfig().getString("formats.sent","[Me -> %player%] %message%");
        String recvFormat = plugin.getConfig().getString("formats.receive","[%player% -> Me] %message%");

        target.sendMessage(mm.deserialize(recvFormat.replace("%player%", p.getName()).replace("%message%", message)));
        p.sendMessage(mm.deserialize(sendFormat.replace("%player%", target.getName()).replace("%message%", message)));

        MsgCommand.last.put(p.getUniqueId(), other);
        MsgCommand.last.put(other, p.getUniqueId());

        return true;
    }
}
