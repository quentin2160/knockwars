package knockwars.managers;

import knockwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SpawnManager {

    private final Main plugin;
    private Location spawnLocation;

    public SpawnManager(Main plugin) {
        this.plugin = plugin;
        loadSpawn();
    }

    public void setSpawn(Location loc) {
        plugin.getConfig().set("spawn.world", loc.getWorld().getName());
        plugin.getConfig().set("spawn.x", loc.getBlockX());
        plugin.getConfig().set("spawn.y", loc.getBlockY());
        plugin.getConfig().set("spawn.z", loc.getBlockZ());
        plugin.saveConfig();
        plugin.reloadConfig();

        spawnLocation = loc;
        plugin.getLogger().info("Spawn KnockWars défini à : " + loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
    }

    public void loadSpawn() {
        String worldName = plugin.getConfig().getString("spawn.world");
        if (worldName == null) {
            spawnLocation = null;
            plugin.getLogger().info("Spawn KnockWars non défini dans la configuration.");
            return;
        }

        // Vérifier que le monde existe
        if (!worldExists(worldName)) {
            spawnLocation = null;
            plugin.getLogger().warning("❌ ERREUR : Le monde KnockWars '" + worldName + "' n'existe pas !");
            plugin.getLogger().warning("Mondes disponibles : " + getAvailableWorlds());
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            spawnLocation = null;
            plugin.getLogger().warning("❌ Le monde KnockWars '" + worldName + "' n'est pas chargé !");
            return;
        }

        int x = plugin.getConfig().getInt("spawn.x");
        int y = plugin.getConfig().getInt("spawn.y");
        int z = plugin.getConfig().getInt("spawn.z");

        spawnLocation = new Location(world, x + 0.5, y, z + 0.5);
        plugin.getLogger().info("✓ Spawn KnockWars chargé : " + worldName + " " + x + " " + y + " " + z);
    }

    /**
     * Vérifie si un monde existe
     */
    public boolean worldExists(String worldName) {
        return Bukkit.getWorld(worldName) != null;
    }

    /**
     * Retourne la liste des mondes disponibles
     */
    private String getAvailableWorlds() {
        StringBuilder worlds = new StringBuilder();
        for (World w : Bukkit.getWorlds()) {
            if (worlds.length() > 0) worlds.append(", ");
            worlds.append(w.getName());
        }
        return worlds.length() > 0 ? worlds.toString() : "Aucun";
    }

    public Location getSpawn() {
        return spawnLocation;
    }

    public boolean hasSpawn() {
        return spawnLocation != null;
    }
}