package knockwars.listeners;

import knockwars.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Empêche un joueur en jeu de jeter les items de son kit KnockWars
 * (arc, bâton, flèche "cachée" du slot 10, porte de sortie).
 * Sans ça, un joueur peut jeter sa flèche et perdre l'infinité de son arc,
 * ou se débarrasser de son item "Quitter la partie".
 */
public class ItemProtectionListener implements Listener {

    private final Main plugin;

    public ItemProtectionListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (plugin.isPlayerInGame(p)) {
            e.setCancelled(true);
        }
    }
}
