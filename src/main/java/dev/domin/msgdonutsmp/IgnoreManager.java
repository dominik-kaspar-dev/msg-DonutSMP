package dev.domin.msgdonutsmp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class IgnoreManager {

    private final JavaPlugin plugin;
    private final File dataFile;
    private FileConfiguration cfg;

    // owner UUID -> set of ignored UUIDs
    private final Map<UUID, Set<UUID>> ignores = new HashMap<>();

    public IgnoreManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        load();
    }

    @SuppressWarnings("unchecked")
    public void load() {
        if (!dataFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
        this.cfg = YamlConfiguration.loadConfiguration(dataFile);
        ignores.clear();
        if (cfg.contains("ignores")) {
            for (String owner : cfg.getConfigurationSection("ignores").getKeys(false)) {
                List<String> list = cfg.getStringList("ignores." + owner);
                Set<UUID> set = list.stream().map(UUID::fromString).collect(Collectors.toSet());
                ignores.put(UUID.fromString(owner), set);
            }
        }
    }

    public void save() {
        try {
            plugin.getDataFolder().mkdirs();
            for (Map.Entry<UUID, Set<UUID>> e : ignores.entrySet()) {
                List<String> list = e.getValue().stream().map(UUID::toString).collect(Collectors.toList());
                cfg.set("ignores." + e.getKey().toString(), list);
            }
            cfg.save(dataFile);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save data.yml: " + ex.getMessage());
        }
    }

    public boolean isIgnored(UUID owner, UUID target) {
        return ignores.getOrDefault(owner, Collections.emptySet()).contains(target);
    }

    public void addIgnore(UUID owner, UUID target) {
        ignores.computeIfAbsent(owner, k -> new HashSet<>()).add(target);
        save();
    }

    public void removeIgnore(UUID owner, UUID target) {
        Set<UUID> set = ignores.get(owner);
        if (set != null) {
            set.remove(target);
            save();
        }
    }

    public Set<UUID> getIgnored(UUID owner) {
        return Collections.unmodifiableSet(ignores.getOrDefault(owner, Collections.emptySet()));
    }

    public int getTotalIgnoredCount() {
        return ignores.values().stream().mapToInt(Set::size).sum();
    }
}
