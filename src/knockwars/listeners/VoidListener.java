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
        // Ne s'applique qu'aux joueurs en jeu, sur le monde du spawn KnockWars.
        // Sans ce check, n'importe quel joueur du serveur tombant sous Y=0
        // (dans un autre monde/une autre partie) se retrouvait téléporté ici.
        if (!plugin.isPlayerInGame(p)) return;

        SpawnManager spawnManager = plugin.getSpawnManager();
        if (!spawnManager.isSpawnWorld(p.getWorld())) return;

        if (e.getTo().getY() < 0) {
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
