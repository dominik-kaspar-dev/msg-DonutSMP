package dev.domin.msgdonutsmp;

import dev.domin.msgdonutsmp.commands.MsgCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.stream.Collectors;

public class MsgPlaceholder extends PlaceholderExpansion {

    private final MsgDonutSMP plugin;

    public MsgPlaceholder(MsgDonutSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "msgdonut";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().isEmpty() ? "unknown" : plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) return "";
        switch (identifier) {
            case "last_partner": {
                UUID u = MsgCommand.last.get(p.getUniqueId());
                if (u == null) return "none";
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(u);
                return formatPlayerName(p, op);
            }
            case "ignored_count":
                return String.valueOf(plugin.getIgnoreManager().getIgnored(p.getUniqueId()).size());
            case "ignored_list":
                return plugin.getIgnoreManager().getIgnored(p.getUniqueId()).stream()
                        .map(uuid -> {
                            OfflinePlayer op = plugin.getServer().getOfflinePlayer(uuid);
                            return formatPlayerName(p, op);
                        }).collect(Collectors.joining(", "));
        }
        return null;
    }

    private String formatPlayerName(Player viewer, OfflinePlayer target) {
        String name = target.getName() == null ? target.getUniqueId().toString() : target.getName();
        if (!plugin.getConfig().getBoolean("placeholderapi.differentiate-vanished", true)) {
            return name;
        }

        Player onlineTarget = target.getPlayer();
        if (onlineTarget != null && plugin.isVanished(viewer, onlineTarget)) {
            String suffix = plugin.getConfig().getString("placeholderapi.vanished-suffix", " (vanished)");
            return name + suffix;
        }

        return name;
    }
}
