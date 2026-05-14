package dev.domin.msgdonutsmp;

import dev.domin.msgdonutsmp.commands.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bstats.charts.SimplePie;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MsgDonutSMP extends JavaPlugin {

    private IgnoreManager ignoreManager;
    private final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.ignoreManager = new IgnoreManager(this);

        // Commands
        getCommand("msg").setExecutor(new MsgCommand(this));
        getCommand("r").setExecutor(new ReplyCommand(this));
        getCommand("msgignore").setExecutor(new IgnoreCommand(this));
        getCommand("msgunignore").setExecutor(new UnignoreCommand(this));
        getCommand("msgignorelist").setExecutor(new IgnoreListCommand(this));
        getCommand("msgreload").setExecutor(new ReloadCommand(this));

        // Listeners
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        // bStats (set your id)
        try {
            int bstatsId = 31310;
            Metrics metrics = new Metrics(this, bstatsId);
            // basic charts
            try {
                metrics.addCustomChart(new SimplePie("allow_ignore_staff", () -> getConfig().getBoolean("options.allow-ignore-staff", true) ? "true" : "false"));
            } catch (Throwable ignored) {}
            try {
                metrics.addCustomChart(new SingleLineChart("ignored_total", () -> getIgnoreManager().getTotalIgnoredCount()));
            } catch (Throwable ignored) {}
        } catch (NoClassDefFoundError ignored) {
        }

        // PlaceholderAPI expansion
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                new MsgPlaceholder(this).register();
            } catch (NoClassDefFoundError ignored) {
            }
        }
    }

    @Override
    public void onDisable() {
        if (ignoreManager != null) ignoreManager.save();
    }

    public IgnoreManager getIgnoreManager() {
        return ignoreManager;
    }

    public boolean isVanished(Player viewer, Player target) {
        if (viewer == null || target == null) {
            return false;
        }

        if (!viewer.canSee(target)) {
            return true;
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                for (String placeholder : getVanishPlaceholders()) {
                    String value = PlaceholderAPI.setPlaceholders(target, placeholder);
                    if ("true".equalsIgnoreCase(value)) {
                        return true;
                    }
                }
            } catch (NoClassDefFoundError ignored) {
            }
        }

        return false;
    }

    private List<String> getVanishPlaceholders() {
        List<String> placeholders = getConfig().getStringList("placeholderapi.vanish-placeholders");
        if (placeholders.isEmpty()) {
            return List.of("%essentials_vanished%", "%supervanish_is_vanished%");
        }
        return placeholders;
    }

    public Component parse(String path) {
        String raw = getConfig().getString(path, "");
        return mm.deserialize(raw);
    }

    public Component parse(String path, java.util.Map<String, String> replacements) {
        String raw = getConfig().getString(path, "");
        if (replacements != null) {
            for (var e : replacements.entrySet()) raw = raw.replace(e.getKey(), e.getValue());
        }
        return mm.deserialize(raw);
    }

    public void send(CommandSender to, String path) {
        to.sendMessage(parse(path));
    }

    public void send(CommandSender to, String path, java.util.Map<String, String> replacements) {
        to.sendMessage(parse(path, replacements));
    }
}
