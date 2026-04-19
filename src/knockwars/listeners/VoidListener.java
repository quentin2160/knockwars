package knockwars.listeners;

import knockwars.Main;
import knockwars.managers.SpawnManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidListener implements Listener {

    private final Main plugin;

    public VoidListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getTo() == null) return;

        Player p = e.getPlayer();
        if (e.getTo().getY() < 0) {
            SpawnManager spawnManager = plugin.getSpawnManager();
            if (spawnManager.hasSpawn()) {
                p.teleport(spawnManager.getSpawn());
                ConfigurationSection lang = plugin.getConfig().getConfigurationSection("lang");
                String msg = "Vous êtes tombé !";
                if (lang != null) {
                    msg = lang.getString("void_respawn", msg);
                }
                p.sendMessage(msg);
            }
        }
    }
}
