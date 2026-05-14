package dev.domin.msgdonutsmp.commands;

import dev.domin.msgdonutsmp.MsgDonutSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class IgnoreListCommand implements CommandExecutor {

    private final MsgDonutSMP plugin;

    public IgnoreListCommand(MsgDonutSMP plugin) {
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
            p.sendMessage(plugin.getConfig().getString("messages.no-permission","&cNo permission."));
            return true;
        }

        var set = plugin.getIgnoreManager().getIgnored(p.getUniqueId());
        int size = Math.max(9, ((set.size() + 8) / 9) * 9);
        Inventory inv = Bukkit.createInventory(null, size, plugin.getConfig().getString("gui.title","Ignore List"));

        // filler
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 0; i < size; i++) inv.setItem(i, filler);

        int slot = 0;
        for (UUID uid : set) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(uid);
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(op);
            meta.setDisplayName(op.getName());
            skull.setItemMeta(meta);
            inv.setItem(slot++, skull);
            if (slot >= size) break;
        }

        p.openInventory(inv);
        return true;
    }
}
