package dev.domin.msgdonutsmp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class GUIListener implements Listener {

    private final MsgDonutSMP plugin;

    public GUIListener(MsgDonutSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(plugin.getConfig().getString("gui.title","Ignore List"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (!(e.getWhoClicked() instanceof Player)) return;
            Player p = (Player) e.getWhoClicked();

            // only handle right-clicks to unignore
            if (!e.isRightClick()) return;

            if (e.getCurrentItem().getItemMeta() instanceof SkullMeta) {
                SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
                if (meta.getOwningPlayer() == null) return;
                var off = meta.getOwningPlayer();
                plugin.getIgnoreManager().removeIgnore(p.getUniqueId(), off.getUniqueId());
                p.closeInventory();
                plugin.send(p, "messages.unignored");
            }
        }
    }
}
