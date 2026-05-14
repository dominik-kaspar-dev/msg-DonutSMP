package dev.domin.msgdonutsmp.commands;

import dev.domin.msgdonutsmp.IgnoreManager;
import dev.domin.msgdonutsmp.MsgDonutSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MsgCommand implements CommandExecutor {

    private final MsgDonutSMP plugin;
    private final IgnoreManager ignoreManager;
    // track last conversation partner
    public static final Map<UUID, UUID> last = new HashMap<>();

    public MsgCommand(MsgDonutSMP plugin) {
        this.plugin = plugin;
        this.ignoreManager = plugin.getIgnoreManager();
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
        if (args.length < 2) {
            plugin.send(p, "messages.msg-usage");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            plugin.send(p, "messages.player-not-found");
            return true;
        }

        if (plugin.isVanished(p, target) && !p.hasPermission("msg.bypass.vanish")) {
            plugin.send(p, "messages.player-not-found");
            return true;
        }

        // ignore check: if target ignores sender, block
        if (ignoreManager.isIgnored(target.getUniqueId(), p.getUniqueId())) {
            plugin.send(p, "messages.ignoring-you");
            return true;
        }

        // build message
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) sb.append(' ');
            sb.append(args[i]);
        }
        String message = sb.toString();
        MiniMessage mm = MiniMessage.miniMessage();

        String sendFormat = plugin.getConfig().getString("formats.sent","[Me -> %player%] %message%");
        String recvFormat = plugin.getConfig().getString("formats.receive","[%player% -> Me] %message%");

        Component send = mm.deserialize(sendFormat.replace("%player%", target.getName()).replace("%message%", message));
        Component recv = mm.deserialize(recvFormat.replace("%player%", p.getName()).replace("%message%", message));

        p.sendMessage(send);
        target.sendMessage(recv);

        // update last
        last.put(p.getUniqueId(), target.getUniqueId());
        last.put(target.getUniqueId(), p.getUniqueId());

        return true;
    }
}
